package com.ilikexy.bigwork;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.baseactivity.ToastAction;
import com.ilikexy.bigwork.constant.ActivityControler;
import com.ilikexy.bigwork.constant.Constant;
import com.ilikexy.bigwork.entity.MyAddFriend;
import com.ilikexy.bigwork.entity.MyContent;
import com.ilikexy.bigwork.entity.MyFound;
import com.ilikexy.bigwork.entity.MyMessage;
import com.ilikexy.bigwork.entity.MyMine;
import com.ilikexy.bigwork.entity.Users;
import com.ilikexy.bigwork.fragment.ContentFragment;
import com.ilikexy.bigwork.fragment.FoundFragment;
import com.ilikexy.bigwork.fragment.MessageFragment;
import com.ilikexy.bigwork.fragment.MineFragment;
import com.ilikexy.bigwork.netaction.SelectAction;
import com.ilikexy.bigwork.recyclerview.AddFriendRecyclerAdapter;
import com.jaeger.library.StatusBarUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainFunctionActivity extends BaseActivity implements View.OnClickListener{
    private List<MyFound> mListMine;//我的列表
    private MyMine myMine;//我得昵称头像数据
    private List<MyMessage> mlistMessage;//消息列表数据
    private List<MyFound> mlistFound;//发现列表数据
    private List<MyContent> mlistContent;//联系人列表
    private FragmentManager manager;//碎片管理
    private FragmentTransaction fragmentTransaction;//碎片转换
    private long touchTime =0;//按下的时间初始化为0
    private Bitmap mbitmap;
    private String mfakename = "昵称";
    public static String usename;

    private TextView text_message_mainfun,text_content_mainfun,text_found_mainfun,text_mine_mainfun;//点击切换页面控件
    private LinearLayout linear_pagechange_mainfun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_function);
        StatusBarUtil.setColor(MainFunctionActivity.this, Color.parseColor("#dddddd"),0);
        StatusBarUtil.setLightMode(MainFunctionActivity.this);
        init();
    }
    //重写导航键按下事件处理函数，主界面按返回提示是否退出应用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){//按返回键，且点击事件是按下
            if ((System.currentTimeMillis()-touchTime)>3000){//系统当前时间减去按下的时间超过3秒
                Toast.makeText(MainFunctionActivity.this,"再按一次返回键，退出应用",Toast.LENGTH_SHORT).show();
                touchTime = System.currentTimeMillis();//将系统的当前时间赋值给touchtime
            }else{//调用BaseActivity中的销毁所有活动的方法
                ActivityControler.destroyActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
    //初始化函数
    public void init(){
        mbitmap = BitmapFactory.decodeResource(getResources(),Constant.DEFAULT_TOUXIANG);//初始化头像
        manager = getSupportFragmentManager();//碎片狗男女， manager和 transaction
        fragmentTransaction = manager.beginTransaction();
        mlistMessage = new ArrayList<MyMessage>();//消息列表数据
        mlistFound = new ArrayList<MyFound>();//发现列表数据
        setMessageFragmentData();
        setFoundFragmentData();
        setMineFragmentData();
        fragmentTransaction.add(R.id.frame_mainfunction,new MessageFragment(mlistMessage));
        fragmentTransaction.commit();
        linear_pagechange_mainfun = (LinearLayout)findViewById(R.id.linear_pagechange_main);
        text_message_mainfun = (TextView)findViewById(R.id.text_message_mainfun);
        text_content_mainfun = (TextView)findViewById(R.id.text_content_mainfun);
        text_found_mainfun = (TextView)findViewById(R.id.text_found_mainfun);
        text_mine_mainfun = (TextView)findViewById(R.id.text_mine_mainfun);
        text_message_mainfun.setOnClickListener(this);
        text_content_mainfun.setOnClickListener(this);
        text_found_mainfun.setOnClickListener(this);
        text_mine_mainfun.setOnClickListener(this);
    }
    //获取系统时间方法
    public String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return String.valueOf(simpleDateFormat.format(date));
    }
    //给MessageFragment提供消息数据
    public void setMessageFragmentData(){
         //第一条消息
        MyMessage myMessage1 = new MyMessage("4121组研发中心","欢迎使用方信，使用中有什么问题可向4121组反馈",
                getDate(), BitmapFactory.decodeResource(getResources(),R.drawable.yanfazu));
        MyMessage myMessage2 = new MyMessage("客户端负责人","你好，我是方啸云！",
                getDate(), BitmapFactory.decodeResource(getResources(),R.drawable.fang));
        MyMessage myMessage3 = new MyMessage("服务器负责人","你好，我是杨财源！",
                getDate(), BitmapFactory.decodeResource(getResources(),R.drawable.yang));
        mlistMessage.add(myMessage1);
        mlistMessage.add(myMessage2);
        mlistMessage.add(myMessage3);
    }
    //给FoundFragment提供消息数据
    public void setFoundFragmentData(){
        //第一条功能
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.pengyouquan);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.saoyisao);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.yaoyiyao);
        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(),R.drawable.kanyikan);
        Bitmap bitmap5 = BitmapFactory.decodeResource(getResources(),R.drawable.souyisou);
        Bitmap bitmap6 = BitmapFactory.decodeResource(getResources(),R.drawable.fujianderen);
        Bitmap bitmap7 = BitmapFactory.decodeResource(getResources(),R.drawable.gouwu);
        Bitmap bitmap8 = BitmapFactory.decodeResource(getResources(),R.drawable.youxi);
        Bitmap bitmap9 = BitmapFactory.decodeResource(getResources(),R.drawable.xiaochengxu);
        MyFound myFound1 = new MyFound(bitmap1,"朋友圈","",false,true);
        MyFound myFound2 = new MyFound(bitmap2,"扫一扫","",true,false);
        MyFound myFound3 = new MyFound(bitmap3,"摇一摇","",false,true);
        MyFound myFound4 = new MyFound(bitmap4,"看一看","",true,false);
        MyFound myFound5 = new MyFound(bitmap5,"搜一搜","",false,true);
        MyFound myFound6 = new MyFound(bitmap6,"附近的人 ","",false,true);
        MyFound myFound7 = new MyFound(bitmap7,"购物","",true,false);
        MyFound myFound8 = new MyFound(bitmap8,"游戏 ","",false,true);
        MyFound myFound9 = new MyFound(bitmap9,"小程序","",false,true);
        mlistFound.add(myFound1);
        mlistFound.add(myFound2);
        mlistFound.add(myFound3);
        mlistFound.add(myFound4);
        mlistFound.add(myFound5);
        mlistFound.add(myFound6);
        mlistFound.add(myFound7);
        mlistFound.add(myFound8);
        mlistFound.add(myFound9);

    }

    //给mineFragment提供头像昵称，方信号数据，以及列表数据
    public void setMineFragmentData(){
        //首先是头像昵称，方信号数据
        //从本地获取账号，查询数据库，返回图片
        SharedPreferences share = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS,MODE_PRIVATE);
        usename = share.getString("usename","");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call call =new SelectAction().selectFormation(usename,"fakename");
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure( Call call,  IOException e) {//网络请求失败
                        Looper.prepare();
                        ToastAction.startToast(MainFunctionActivity.this,"网络出错，请重试！");
                        Looper.loop();
                    }
                    @Override
                    public void onResponse( Call call, Response response) throws IOException {
                        Looper.prepare();
                        final String getresult = response.body().string();
                        Log.d("ooo",getresult);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            Log.d("oooo",getresult);
                           // mbitmap = new BitmapAction().stringToBitmap(getresult);//图片
                            mfakename = getresult;//子线程赋值给主线程
                            //把这个存入缓存，从别的地方要用时
                            SharedPreferences.Editor editor = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS,MODE_PRIVATE).edit();
                            editor.putString("fakename",mfakename);
                            editor.commit();
                            }
                        });
                        Looper.loop();
                    }
                });
            }
        }).start();

        //然后是数据
        mListMine = new ArrayList<MyFound>();
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.zhifu);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.shoucang);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.xiangce);
        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(),R.drawable.kabao);
        Bitmap bitmap5 = BitmapFactory.decodeResource(getResources(),R.drawable.biaoqin);
        Bitmap bitmap6 = BitmapFactory.decodeResource(getResources(),R.drawable.shezhi);
        MyFound myFound1 = new MyFound(bitmap1,"支付","",false,true);
        MyFound myFound2 = new MyFound(bitmap2,"收藏","",true,false);
        MyFound myFound3 = new MyFound(bitmap3,"相册","",true,false);
        MyFound myFound4 = new MyFound(bitmap4,"卡包","",true,false);
        MyFound myFound5 = new MyFound(bitmap5,"表情","",false,true);
        MyFound myFound6 = new MyFound(bitmap6,"设置","",true,false);
        mListMine.add(myFound1);
        mListMine.add(myFound2);
        mListMine.add(myFound3);
        mListMine.add(myFound4);
        mListMine.add(myFound5);
        mListMine.add(myFound6);
    }
    //给 contentFragment提供 联系人图片，联系人名字
    public void setContentFragmentData(){
        mlistContent  = new ArrayList<MyContent>();//初始化列表
        //网络请求获得联系人
        new Thread(new Runnable() {//网络请求
            @Override
            public void run() {
                Call call = new SelectAction().selectFriend(usename);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure( Call call,IOException e) {
                        Looper.prepare();
                        ToastAction.startToast(MainFunctionActivity.this,"网络出错了！");
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
                                    Log.d("hahaha",jsoner);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                parseJsonAndDoList(jsoner);
                                fragmentTransaction = manager.beginTransaction();
                                fragmentTransaction.replace(R.id.frame_mainfunction,new ContentFragment(mlistContent));
                                linear_pagechange_mainfun.setBackground(getDrawable(R.drawable.messagerr2));
                                fragmentTransaction.commit();
                            }
                        });

                    }
                });
            }
        }).start();

    }
    //从服务器中获取联系人数据
    public void parseJsonAndDoList(String jsoner){
        if(!jsoner.equals("暂无好友")){//有好友
            Gson gson = new Gson();//Gson解析对象
            List<Users> list = new ArrayList<Users>();//接收实体对象
            List<MyContent> listmyContent= new ArrayList<MyContent>();//list可添加联系人对象
            list = gson.fromJson(jsoner,new TypeToken<List<Users>>(){}.getType());

            //头像随机
            Bitmap bitmap11 = BitmapFactory.decodeResource(getResources(),R.drawable.xinyi);
            Bitmap bitmap12 = BitmapFactory.decodeResource(getResources(),R.drawable.ruming);
            Bitmap bitmap13 = BitmapFactory.decodeResource(getResources(),R.drawable.zhongrongquan);
            Bitmap bitmap14 = BitmapFactory.decodeResource(getResources(),R.drawable.yangyan);
            Bitmap bitmap15 = BitmapFactory.decodeResource(getResources(),R.drawable.yuanjiao);
            Bitmap bitmap16 = BitmapFactory.decodeResource(getResources(),R.drawable.dingshuwen);
            Bitmap bitmap17 = BitmapFactory.decodeResource(getResources(),R.drawable.meidongwang);
            Bitmap bitmap18 = BitmapFactory.decodeResource(getResources(),R.drawable.guanqing);
            Bitmap bitmapyuan = BitmapFactory.decodeResource(getResources(),R.drawable.yuan);
            Bitmap bitmapyun = BitmapFactory.decodeResource(getResources(),R.drawable.yun);
            Bitmap[] bitmaps = new Bitmap[8];
            bitmaps[0] = bitmap11;
            bitmaps[1] = bitmap12;
            bitmaps[2] = bitmap13;
            bitmaps[3] = bitmap14;
            bitmaps[4] = bitmap15;
            bitmaps[5] = bitmap16;
            bitmaps[6] = bitmap17;
            bitmaps[7] = bitmap18;
            for (int i=0;i<list.size();i++){
                //------------------------------------------------------------------
                MyContent myContent = null;
                if(list.get(i).getFakename().equals("杨财源")){
                    myContent= new MyContent(bitmapyuan,list.get(i).getFakename(),
                            list.get(i).getUsename(),false,true);
                }else if(list.get(i).getFakename().equals("方啸云")){
                    myContent= new MyContent(bitmapyun,list.get(i).getFakename(),
                            list.get(i).getUsename(),false,true);
                }else{
                        myContent= new MyContent(bitmaps[i],list.get(i).getFakename(),
                                list.get(i).getUsename(),false,true);

                }
                //------------------------------------------------------------------------------
                listmyContent.add(myContent);//----------------------------------------联系人
            }
            mlistContent = listmyContent;
        }else{//无可添加好友
            List<MyContent> listmyaddfriend = new ArrayList<MyContent>();//--------------联系人
            mlistContent = listmyaddfriend;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Bitmap mbitmap1 = getpictureLocal();
        if (mbitmap1!=null){
            mbitmap = mbitmap1;
        }
        setMineFragmentData();
    }

    @Override
    public void onClick(View view) {
        fragmentTransaction = manager.beginTransaction();
        switch (view.getId()){
            case R.id.text_message_mainfun:
                fragmentTransaction.replace(R.id.frame_mainfunction,new MessageFragment(mlistMessage));
                linear_pagechange_mainfun.setBackground(getDrawable(R.drawable.messagerr1));
                break;
            case R.id.text_content_mainfun:
                setContentFragmentData();
                break;
            case R.id.text_found_mainfun:
                fragmentTransaction.replace(R.id.frame_mainfunction,new FoundFragment(mlistFound));
                linear_pagechange_mainfun.setBackground(getDrawable(R.drawable.messagerr3));
                break;
            case R.id.text_mine_mainfun://个人信息维护
                //头像存数据库存在一些问题，所以，我们决定先存本地
                myMine = new MyMine(mbitmap,mfakename,usename);
               // myMine = new MyMine(mbitmap,mfakename,usename);
                fragmentTransaction.replace(R.id.frame_mainfunction,new MineFragment(mListMine,myMine));
                linear_pagechange_mainfun.setBackground(getDrawable(R.drawable.messagerr4));
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }
    //从本地读取
    public Bitmap getpictureLocal() {
        SharedPreferences sharedPreferencess = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE);
        String toto = sharedPreferencess.getString("picture", "");
        if (!toto.equals("")) {
            byte[] buff = Base64.decode(toto, Base64.DEFAULT);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(buff);
            Drawable drawable = Drawable.createFromStream(inputStream, "");
            if (drawable == null) {
                Toast.makeText(MainFunctionActivity.this, "drawable为空", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                return bitmap;
            }
        } else {
            Toast.makeText(MainFunctionActivity.this, "没有sharepreferences", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}