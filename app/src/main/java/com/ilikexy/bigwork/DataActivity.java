package com.ilikexy.bigwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.baseactivity.ToastAction;
import com.ilikexy.bigwork.constant.Constant;
import com.ilikexy.bigwork.entity.MyFormation;
import com.ilikexy.bigwork.entity.Users;
import com.ilikexy.bigwork.netaction.SelectAction;
import com.ilikexy.bigwork.recyclerview.DataRecyclerAdapter;
import com.ilikexy.bigwork.zidingyi.DialogContainer;
import com.ilikexy.bigwork.zidingyi.RoundedSquare;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataActivity extends BaseActivity implements View.OnClickListener {
    //按钮类, 返回键
    private TextView mTextBack;
    //填充数据text类，头顶备注名，昵称，方信号，所在地
    private TextView mBeizhuTop,mTextFakename,mTextUsename,mTextAddress;
    //recyclervView
    private RecyclerView mRecyclerview;
    private DataRecyclerAdapter mDataRecyclerAdapter;
    //联系人图片
    private RoundedSquare mRoundedSquare;
    //跳出dialog，
    private ImageView mImageMenu;
    //发送消息
    private LinearLayout mLinearSendMessage;
    //广播
    private ContentDataBroadcast contentDataBroadcast;//联系人信息的广播
    private ChangeBeiZhuBroadcast changeBeiZhuBroadcast;//修改备注的广播
    private DeleteFriendBroadcast deleteFriendBroadcast;//删除好友的广播
    //部分数据初始默认值
    private String mSex = "暂无";
    private String mAge = "0";
    private String mHomeTown = "暂无";
    private String mSignature = "暂无";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        StatusBarUtil.setColor(DataActivity.this, Color.parseColor("#ffffff"),0);
        StatusBarUtil.setLightMode(DataActivity.this);
        init();
    }
    //获得联系人名字
    public String getFriendName(){
        Intent intent = getIntent();
        String getname = intent.getStringExtra("contentname");
        return getname;
    }
    //获得联系人的usename
    public String getFriendUsename(){
        Intent intent = getIntent();
        String getusename = intent.getStringExtra("contentusename");
        return getusename;
    }
    //控件初始化
    public void init(){
        mTextBack = (TextView)findViewById(R.id.text_back_dataactivity);
        mImageMenu = (ImageView)findViewById(R.id.imag_menu_dataactivity);
        mLinearSendMessage = (LinearLayout) findViewById(R.id.linear_sendme_dataactivity);
        mBeizhuTop = (TextView)findViewById(R.id.text_beizhu_dataactivity);
        mTextFakename = (TextView)findViewById(R.id.text_fakename_dataactivity);
        mTextUsename = (TextView)findViewById(R.id.text_usename_dataactivity);
        mTextAddress = (TextView)findViewById(R.id.text_address_dataactivity);
        mRecyclerview = (RecyclerView)findViewById(R.id.recycler_forma_dataactivity);
        mRoundedSquare = (RoundedSquare)findViewById(R.id.imag_content_dataactivity);
        registerBroad();//注册广播
        //一些控件点击事件处理
        mTextBack.setOnClickListener(this);//返回键点击处理
        mImageMenu.setOnClickListener(this);//菜单点击处理
        mLinearSendMessage.setOnClickListener(this);//发送消息按钮点击处理
        //联系人数据默认初始化数据
        SharedPreferences sharedPreferencess = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE);
        String toto = sharedPreferencess.getString("contentpicture", "");
        if (!toto.equals("")) {
            byte[] buff = Base64.decode(toto, Base64.DEFAULT);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(buff);
            Drawable drawable = Drawable.createFromStream(inputStream, "");
            if (drawable == null) {
                Toast.makeText(DataActivity.this, "drawable为空", Toast.LENGTH_SHORT).show();
                mRoundedSquare.setMbitmap(new BitmapFactory().decodeResource(getResources(),R.drawable.graytouxiang));
            } else {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                mRoundedSquare.setMbitmap(bitmap);
            }
        }

        mBeizhuTop.setText(getFriendName());
        mTextUsename.setText(getFriendUsename());
        mTextAddress.setText("暂无");

        //网络请求获得联系人相关信息
        doNetAction();

    }
    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.text_back_dataactivity:
                 onBackPressed();
                 break;
             case R.id.imag_menu_dataactivity://点击菜单，弹出底部dialog，选择删除好友，修改备注，以及取消
                 DialogContainer.showContentDialog(this,mBeizhuTop);
                 break;
             case R.id.linear_sendme_dataactivity://发送消息
                 jumptoChatActivity();
                 break;
             default:
                 break;
         }
    }
    //跳转到聊天活动
    public void jumptoChatActivity(){
        Intent intent = new Intent();
        intent.putExtra("friendusename",getFriendUsename());
        intent.putExtra("friendfakename",getFriendName());
        intent.setClass(DataActivity.this,ChatActivity.class);
        startActivity(intent);
    }
    //网络请求操作
    public void doNetAction(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call call  = new SelectAction().selectFormation(getFriendUsename());//传入联系人的账号
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure( Call call, IOException e) {

                    }//没网络，请求失败
                    @Override
                    public void onResponse(Call call,Response response) throws IOException {
                        String getdata = response.body().string();//从数据库中获得的json数据
                        Gson gson = new Gson();
                        final Users users = gson.fromJson(getdata,new TypeToken<Users>(){}.getType());//用户实体去接收
                        //开启ui线程切换为主线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (users.getFakename()!=null){
                                   mTextFakename.setText(users.getFakename());
                                }
                                if (users.getSex()!=null){
                                    mSex = users.getSex();
                                }
                                if (users.getAge()!=null){
                                    mAge = users.getAge();
                                }
                                if (users.getHometown()!=null){
                                    mHomeTown = users.getHometown();
                                }
                                if (users.getAddress()!=null){
                                    mTextAddress.setText(users.getAddress());
                                }
                                if (users.getSignature()!=null){
                                    mSignature = users.getSignature();
                                }
                                updateRecyclerdata();
                            }
                        });

                    }
                });
            }
        }).start();
    }
    //发送广播，更新列表数据
    public void updateRecyclerdata(){
        Intent intent = new Intent("com.ilikexy.bigwork.contentdata");
        sendBroadcast(intent);
    }
    //recyclerview 数据填充
    public void setDataData(){
        List<MyFormation> myFormationList = new ArrayList<MyFormation>();
        MyFormation myFormation1 = new MyFormation("性别",mSex,true,false);
        MyFormation myFormation2 = new MyFormation("年龄",mAge,false,true);
        MyFormation myFormation3 = new MyFormation("家乡",mHomeTown,true,false);
        MyFormation myFormation4 = new MyFormation("个性签名",mSignature,false,true);

        myFormationList.add(myFormation1);
        myFormationList.add(myFormation2);
        myFormationList.add(myFormation3);
        myFormationList.add(myFormation4);
        //列表数据
        mDataRecyclerAdapter = new DataRecyclerAdapter(myFormationList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(manager);
        mRecyclerview.setAdapter(mDataRecyclerAdapter);
    }
    //注册广播
    public void registerBroad(){
        IntentFilter intentFilter = new IntentFilter("com.ilikexy.bigwork.contentdata");
        contentDataBroadcast = new ContentDataBroadcast();
        registerReceiver(contentDataBroadcast,intentFilter);//动态注册广播
        IntentFilter intentFilter1 = new IntentFilter("com.ilikexy.bigwork.changebeizhu");
        changeBeiZhuBroadcast = new ChangeBeiZhuBroadcast();
        registerReceiver(changeBeiZhuBroadcast,intentFilter1);//动态注册广播
        IntentFilter intentFilter2 = new IntentFilter("com.ilikexy.bigwork.deletefriend");
        deleteFriendBroadcast = new DeleteFriendBroadcast();
        registerReceiver(deleteFriendBroadcast,intentFilter2);//动态注册广播
    }



    //广播
    class ContentDataBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            setDataData();//广播接收后的操作就是提醒更新ui
        }
    }
    //修改备注的广播
    class ChangeBeiZhuBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String getbeizhu = intent.getStringExtra("beizhu");//备注名，还要有此用户usename,好友usename
            changeBeizhuAction(MainFunctionActivity.usename,getFriendUsename(),getbeizhu);
        }
    }
    //删除好友广播
    class DeleteFriendBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            deleteFriend();
        }
    }
    //修改备注的网络操作
    public void changeBeizhuAction(String usename,String friendusename,String beizhu){
        Call call = new SelectAction().changeBeizhu(usename,friendusename,beizhu);
        call.enqueue(new Callback() {
            @Override
            public void onFailure( Call call, IOException e) {
                Looper.prepare();
                ToastAction.startToast(DataActivity.this,"网络未连接");
                Looper.loop();
            }
            @Override
            public void onResponse( Call call,Response response) throws IOException {
               final String getdataer = response.body().string();
                Looper.prepare();
               if (!getdataer.equals("修改备注失败")&&!getdataer.equals("参数错误")){
                   mBeizhuTop.post(new Runnable() {
                       @Override
                       public void run() {
                           mBeizhuTop.setText(getdataer);//设置控件的备注
                       }
                   });
                   ToastAction.startToast(DataActivity.this,"成功修改备注");
               }else{
                   ToastAction.startToast(DataActivity.this,getdataer);
               }
               Looper.loop();
            }
        });
    }

    //删除好友
    public void deleteFriend(){
        Call call = new SelectAction().DeleteFriend(MainFunctionActivity.usename,getFriendUsename());//删除好友
        call.enqueue(new Callback() {
            @Override
            public void onFailure( Call call,IOException e) {
                Looper.prepare();
                ToastAction.startToast(DataActivity.this,"网络未连接");
                Looper.loop();
            }
            @Override
            public void onResponse( Call call, Response response) throws IOException {
                 String getdeleteresult = response.body().string();

                 if (getdeleteresult.equals("删除好友成功")){
                     Looper.prepare();
                     ToastAction.startToast(DataActivity.this,"删除好友成功");
                     DataActivity.this.finish();
                     Looper.loop();
                 }else{
                     Looper.prepare();
                     ToastAction.startToast(DataActivity.this,getdeleteresult);
                     Looper.loop();
                 }

            }
        });
    }

    //销毁广播

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(contentDataBroadcast);
        unregisterReceiver(changeBeiZhuBroadcast);
        unregisterReceiver(deleteFriendBroadcast);
    }
}