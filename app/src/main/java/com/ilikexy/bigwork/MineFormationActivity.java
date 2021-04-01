package com.ilikexy.bigwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.constant.Constant;
import com.ilikexy.bigwork.entity.MyFormation;
import com.ilikexy.bigwork.entity.Users;
import com.ilikexy.bigwork.media.CaptureClass;
import com.ilikexy.bigwork.netaction.SelectAction;
import com.ilikexy.bigwork.photouri.DcimUriget;
import com.ilikexy.bigwork.photouri.PicCutAndScale;
import com.ilikexy.bigwork.recyclerview.FormaRecyclerAdapter;
import com.ilikexy.bigwork.zidingyi.DialogContainer;
import com.ilikexy.bigwork.zidingyi.RoundedSquare;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MineFormationActivity extends BaseActivity implements View.OnClickListener{
    private TextView mTextBack;
    private RoundedSquare mRoundedSquare;
    private Bitmap mbitmap;//头像图片
    private LinearLayout mLinear;
    private RecyclerView mRecycler;
    //相机，照片通知类
    private PaizhaoRecieverOne paizhaoReceiverOne;
    private XiangceRecieverOne xiangceReceiverOne;
    private FormaReciever formaReciever;
    //图片全局uri
    private Uri mPhotoUri;
    //全局属性变量如下
    private String mfakenamer,msex,mage,mhometown,maddress,msignature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_formation);
        StatusBarUtil.setColor(MineFormationActivity.this, Color.parseColor("#ededed"),0);
        StatusBarUtil.setLightMode(MineFormationActivity.this);
        init();
        broadcastPicture();
    }
    //初始化
    public void init(){
        //全局变量初始化
        mfakenamer="昵称";
        msex = "男";
        mage = "0";
        mhometown = "未填写";
        maddress = "未填写";
        msignature = "这个人很懒，还没填写";
        //注册通知
        IntentFilter intentFilter = new IntentFilter("com.ilikexy.bigwork.forma");
         formaReciever = new FormaReciever();
        registerReceiver(formaReciever,intentFilter);

        mbitmap = BitmapFactory.decodeResource(getResources(),R.drawable.graytouxiang);//默认头像图片
        mTextBack = (TextView)findViewById(R.id.text_back_forma);
        mRoundedSquare = (RoundedSquare)findViewById(R.id.imag_formation_activity);
        mLinear = (LinearLayout)findViewById(R.id.linear_formation_activity);
        mRecycler = (RecyclerView)findViewById(R.id.recycler_formation_activity);

        mTextBack.setOnClickListener(this);
        mLinear.setOnClickListener(this);
        //头像初始化，从本地的sharepreference中获取，在mine界面对服务器进行请求,这里用默认图片要是share没有的话
        Bitmap mbitmap1 = getpictureLocal();
        if (mbitmap1!=null){
            mbitmap = mbitmap1;
        }
        touxiangInit(mbitmap);
        getData();
    }
    //获取本地图片
    public Bitmap getpictureLocal() {
        SharedPreferences sharedPreferencess = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE);
        String toto = sharedPreferencess.getString("picture", "");
        if (!toto.equals("")) {
            byte[] buff = Base64.decode(toto, Base64.DEFAULT);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(buff);
            Drawable drawable = Drawable.createFromStream(inputStream, "");
            if (drawable == null) {
                Toast.makeText(MineFormationActivity.this, "drawable为空", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                return bitmap;
            }
        } else {
            Toast.makeText(MineFormationActivity.this, "没有sharepreferences", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    //设置头像
    public void touxiangInit(Bitmap c_bitmap){
        mRoundedSquare.setMbitmap(c_bitmap);
    }
    //开始写 头像点击的处理方法。
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_formation_activity:
                //打开相机，相册操作
                openCameraAndDcim();
                break;
            case R.id.text_back_forma:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }

    //列表数据传入
    public void getData(){
        //这里的数据就需要从服务器获取，获取全部的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call call = new SelectAction().selectFormation(MainFunctionActivity.usename);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call,IOException e) {

                    }
                    @Override
                    public void onResponse(Call call,Response response) throws IOException {
                        String jsoner = response.body().string();
                        //解析jsoner, new TypeToken<Users>(){}.getType()
                        Gson gsoner = new Gson();
                        Users users = gsoner.fromJson(jsoner, new TypeToken<Users>(){}.getType());
                        if (users.getFakename()!=null){
                            mfakenamer = users.getFakename();
                        }
                        if (users.getSex()!=null){
                            msex = users.getSex();
                        }
                        if (users.getAge()!=null){
                            mage = users.getAge();
                        }
                        if (users.getHometown()!=null){
                            mhometown = users.getHometown();
                        }
                        if (users.getAddress()!=null){
                            maddress = users.getAddress();
                        }
                        if (users.getSignature()!=null){
                            msignature = users.getSignature();
                        }
                        //发送通知
                        Intent intent = new Intent("com.ilikexy.bigwork.forma");
                        MineFormationActivity.this.sendBroadcast(intent);
                    }
                });
            }
        }).start();

    }


    //-------------------------------------------------------------------------------------------------
    public void openCameraAndDcim(){
        //弹出对话框，一切操作启动都起源于点击对话框
        DialogContainer.showPictureDialog(MineFormationActivity.this,R.layout.dialog_touxiang,
                "com.ilikexy.bigwork.paizhaoone","com.ilikexy.bigwork.xiangceone");
        //在弹出的对话框中，点击后 发送相应的通知，注册启动相机的通知
    }
    //注册通知
    public void broadcastPicture(){
        IntentFilter intentFilter1 = new IntentFilter("com.ilikexy.bigwork.paizhaoone");//拍照action
        IntentFilter intentFilter2 = new IntentFilter("com.ilikexy.bigwork.xiangceone");//相册action
        paizhaoReceiverOne = new PaizhaoRecieverOne();//拍照通知，实现跳转到相机
        xiangceReceiverOne = new XiangceRecieverOne();//相册通知，实现跳转到相册
        registerReceiver(paizhaoReceiverOne,intentFilter1);//动态注册
        registerReceiver(xiangceReceiverOne,intentFilter2);
    }
    //相机通知构建
    class PaizhaoRecieverOne extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {//当接收到通知时的操作
            mPhotoUri = new CaptureClass(MineFormationActivity.this).openCamera();//打开相机
        }
    }
    //相册通知构建
    class XiangceRecieverOne extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new CaptureClass(MineFormationActivity.this).openDcim();//打开相册
        }
    }
    //对于相机活动，相册活动请求结果处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://打开相机
                if (resultCode == RESULT_OK) {
                    try {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mPhotoUri);
                        //具体操作为 切换为主线程，图片压缩，Glide插入相册
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//
                               Bitmap bitmap1 =  PicCutAndScale.getSquareRect(bitmap);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                //****当然设置的图片需要保存至数据库端,这里先保存至本地
                                SharedPreferences.Editor editor = getSharedPreferences(
                                        Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE).edit();
                                //把图片转化为json,存储到sharePreference中
                                String tojsoner = new String(android.util.Base64.encodeToString(baos.toByteArray(),
                                        Base64.DEFAULT));
                                editor.putString("picture", tojsoner);
                                editor.apply();
                                mRoundedSquare.setMbitmap(bitmap1);
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,//插入相册
                                        "Titler", "第一张");
                                //发送更新指定uri的广播, Scanner scan
                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mPhotoUri);
                                sendBroadcast(intent);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MineFormationActivity.this,"拍照失败",Toast.LENGTH_SHORT).show();}
                break;
            case 2:
                if (resultCode==RESULT_OK){//打开相册成功
                    Uri uri = data.getData();//通过data获得图片uri
                    String filePath = DcimUriget.getFilePathByUri(this, uri);//调用根据uri获得图片路径的方法
                    if (!TextUtils.isEmpty(filePath)) {//如果路径不为空
                        //需要Glide加载图片
                        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                        //将照片显示在 ivImage上，根据路径加载图片
                        Glide.with(this).load(filePath).apply(requestOptions1)
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        mRoundedSquare.setMbitmap(((BitmapDrawable)resource).getBitmap());
                                    }
                                });
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);//从路径获取图片
                        //把图片转化为json,存储到sharePreference中
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//bitmap转字节赋值给baos
                        SharedPreferences.Editor editor = getSharedPreferences(
                                Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE).edit();
                        String tojsoner = new String(android.util.Base64.encodeToString(baos.toByteArray(),
                                Base64.DEFAULT));
                        editor.putString("picture", tojsoner);
                        editor.apply();
                    }
                }else{Toast.makeText(MineFormationActivity.this,"打开相册失败",Toast.LENGTH_SHORT).show();}
                break;
            default:
                break;
        }//switchcasef方法
    }
    //存储权限处理
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode){
            case 11:
                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    new CaptureClass(MineFormationActivity.this).openDcimright();//请求成功
                }else{
                    Toast.makeText(this,"拒绝授权，程序销毁！",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
    //在活动销毁时，自然也要解除通知
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(paizhaoReceiverOne);
        unregisterReceiver(xiangceReceiverOne);
        unregisterReceiver(formaReciever);
    }
    //-----------------------------------------------------------------------------------------------

    class FormaReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            List<MyFormation> myFormationList = new ArrayList<MyFormation>();
            MyFormation myFormation1 = new MyFormation("昵称",mfakenamer,false,true);
            MyFormation myFormation2 = new MyFormation("性别",msex,true,false);
            MyFormation myFormation3 = new MyFormation("年龄",mage,true,false);
            MyFormation myFormation4 = new MyFormation("家乡",mhometown,true,false);
            MyFormation myFormation5 = new MyFormation("所在地",maddress,false,true);
            MyFormation myFormation6 = new MyFormation("个性签名",msignature,true,false);


            myFormationList.add(myFormation1);
            myFormationList.add(myFormation2);
            myFormationList.add(myFormation3);
            myFormationList.add(myFormation4);
            myFormationList.add(myFormation5);
            myFormationList.add(myFormation6);
            //列表初始化
            LinearLayoutManager manager = new LinearLayoutManager(MineFormationActivity.this);
            mRecycler.setLayoutManager(manager);
            FormaRecyclerAdapter adapter = new FormaRecyclerAdapter(myFormationList);
            mRecycler.setAdapter(adapter);
        }
    }


}