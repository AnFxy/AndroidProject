package com.ilikexy.bigwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.baseactivity.ToastAction;
import com.ilikexy.bigwork.entity.MyAddFriend;
import com.ilikexy.bigwork.entity.MyAddFriendZ;
import com.ilikexy.bigwork.entity.Users;
import com.ilikexy.bigwork.netaction.SelectAction;
import com.ilikexy.bigwork.recyclerview.AddFriendRecyclerAdapter;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends BaseActivity implements View.OnClickListener{
     //返回按钮，搜索按钮
    private TextView mTextSousuo,mTextBack;
    //方信号输入框
    private EditText mEditText;
    //recyclerview
    private RecyclerView mRecyclerView;
    //private List<MyAddFriend> myAddFriendList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_add_friend);
        StatusBarUtil.setColor(AddFriendActivity.this, Color.parseColor("#ededed"),0);
        StatusBarUtil.setLightMode(AddFriendActivity.this);
        init();
    }
    public void init(){
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_content_addfriend);
        mEditText = (EditText)findViewById(R.id.eidt_sousuo_dataactivity);//方信号输入框
        mTextSousuo = (TextView)findViewById(R.id.text_sousuo_addfriend);//搜索按钮
        mTextBack = (TextView)findViewById(R.id.text_back_addfriend);//返回
        mTextBack.setOnClickListener(this);
        mTextSousuo.setOnClickListener(this);
        new Thread(new Runnable() {//网络请求
            @Override
            public void run() {
                Call call = new SelectAction().selectCanAddFriend(MainFunctionActivity.usename);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure( Call call,IOException e) {
                        Looper.prepare();
                        ToastAction.startToast(AddFriendActivity.this,"网络出错了！");
                        Looper.loop();
                    }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        runOnUiThread(new Runnable() {//子线程切换主线程
                            @Override
                            public void run() {
                                String jsoner = null;//获得的json数据
                                try {
                                    jsoner = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                parseJsonAndDoList(jsoner);
                            }
                        });

                    }
                });
            }
        }).start();
    }
    //对于从数据库接收到的json数据进行解析，处理后赋值给List的实体，
    //根据每个list实体对象，取 usename和fakename构建addfriend实体List,传入 recyclerview
    public void parseJsonAndDoList(String jsoner){
        if(!jsoner.equals("无可添加好友")){//有可添加的好友
            Gson gson = new Gson();//Gson解析对象
            List<Users> list = new ArrayList<Users>();//接收实体对象
            List<MyAddFriend> listmyaddfriend = new ArrayList<MyAddFriend>();//list可添加联系人对象
            list = gson.fromJson(jsoner,new TypeToken<List<Users>>(){}.getType());
            for (int i=0;i<list.size();i++){
                MyAddFriend friend = new MyAddFriend(list.get(i).getUsename(),list.get(i).getFakename(),false);
                listmyaddfriend.add(friend);
            }
            LinearLayoutManager manager = new LinearLayoutManager(AddFriendActivity.this);
            mRecyclerView.setLayoutManager(manager);
            AddFriendRecyclerAdapter adapter = new AddFriendRecyclerAdapter(listmyaddfriend);
            mRecyclerView.setAdapter(adapter);
        }else{//无可添加好友
            List<MyAddFriend> listmyaddfriend = new ArrayList<MyAddFriend>();
            LinearLayoutManager manager = new LinearLayoutManager(AddFriendActivity.this);
            mRecyclerView.setLayoutManager(manager);
            AddFriendRecyclerAdapter adapter = new AddFriendRecyclerAdapter(listmyaddfriend);
            mRecyclerView.setAdapter(adapter);
        }
    }
    //点击事件处理
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_back_addfriend://返回销毁活动
                onBackPressed();
                break;
            case R.id.text_sousuo_addfriend://搜索点击后，网络搜索
                clickSousuoFriend();
                break;
            default:
                break;
        }
    }
    //点击搜索好友按钮后
    public void clickSousuoFriend(){
        String getdataerr =  mEditText.getText().toString();//获得输入框的数据
        if (getdataerr.equals("")){//输入框为空，不予理会
          ToastAction.startToast(AddFriendActivity.this,"输入内容为空");
        }else{//进行网络操作，搜寻这个方信号的用户
            Call call = new SelectAction().SelectFangxin(MainFunctionActivity.usename,getdataerr);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call,IOException e) {
                    Looper.prepare();
                    ToastAction.startToast(AddFriendActivity.this,"网络出错");
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String jsoner1 = null;//获得来自服务器的数据
                            try {
                                jsoner1 = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                           List<MyAddFriend> myAddFriendList11 = new ArrayList<MyAddFriend>();
                            if(jsoner1.equals("查无此人")){//没有这个人
                                //不做任何处理
                            }else{
                                Gson gson = new Gson();
                                MyAddFriendZ myAddFriendz1 = gson.fromJson(jsoner1,new TypeToken<MyAddFriendZ>(){}.getType());
                                boolean isadder = true;
                                if(myAddFriendz1.isAdded().equals("true")){
                                    isadder =  true;
                                }else{
                                    isadder =  false;
                                }
                                MyAddFriend myAddFriender = new MyAddFriend(myAddFriendz1.getMusename(),myAddFriendz1.getMfakename(),
                                       isadder);
                                myAddFriendList11.add(myAddFriender);
                            }
                            LinearLayoutManager manager = new LinearLayoutManager(AddFriendActivity.this);
                            mRecyclerView.setLayoutManager(manager);
                            AddFriendRecyclerAdapter adapter = new AddFriendRecyclerAdapter(myAddFriendList11);
                            mRecyclerView.setAdapter(adapter);
                        }
                    });

                }
            });
        }
    }
}