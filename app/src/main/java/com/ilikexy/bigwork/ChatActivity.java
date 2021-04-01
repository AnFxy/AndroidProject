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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.baseactivity.ToastAction;
import com.ilikexy.bigwork.chat.ChatClass;
import com.ilikexy.bigwork.constant.Constant;
import com.ilikexy.bigwork.entity.MyXinxi;
import com.ilikexy.bigwork.entity.MyXinxitwo;
import com.ilikexy.bigwork.netaction.SelectAction;
import com.ilikexy.bigwork.recyclerview.SendMessRecyclerAdapter;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends BaseActivity implements View.OnClickListener {
    //返回按钮, 好友昵称， 语音功能，表情， 拓展功能
    private TextView mtextBack,mtextFriendname,mtextVoice,mtextEmoji,mtextExpand,mtextSend;
    //输入栏linearlayout
    private LinearLayout mMessageLinear;
    private EditText mEditMessage;
    //以及菜单栏图片
    private ImageView mimagMenu;
    //recyclerview列表
    private RecyclerView mRecyclerMessage;
    private List<MyXinxi> listmyxinxi;
    private String mFriendBeizhu = "备注";
    //广播声明
    private SendMessageBroadcast msendMessageBroadcast;
    private GetMessageBroadcast mgetMessageBroadcast;
    private ChatClass chatClass;
    //聊天记录数据
    private List<MyXinxitwo> listmyxintwo;
    //自己的图片
    private Bitmap bitmapself;
    private Bitmap bitmapother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_chat);
        StatusBarUtil.setColor(ChatActivity.this, Color.parseColor("#ededed"),0);
        StatusBarUtil.setLightMode(ChatActivity.this);
        init();
        edittextFuncton();

    }
    //获得朋友的账号和昵称
    public String getfriendusename(){
        Intent intent = getIntent();
        String usename = intent.getStringExtra("friendusename");
        return usename;
    }
    //获得朋友的备注名
    public void getfriendfakename(){
        //网络请求
        Call call = new SelectAction().SelectFriendBeizhu(MainFunctionActivity.usename,getfriendusename());
        call.enqueue(new Callback() {
            @Override
            public void onFailure( Call call,IOException e) {
                Looper.prepare();
                ToastAction.startToast(ChatActivity.this,"获取备注失败");
                Looper.loop();
            }
            @Override
            public void onResponse( Call call,Response response) throws IOException {
               final String beizhu = response.body().string();//从服务器中获取到的备注名
                mFriendBeizhu = beizhu;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mtextFriendname.setText(beizhu);
                    }
                });
            }
        });
    }
    public void init(){
        mtextBack = (TextView)findViewById(R.id.text_back_chatactivity);
        mtextFriendname = (TextView)findViewById(R.id.text_friendname_chatactivity);
        mtextVoice = (TextView)findViewById(R.id.text_voice_chatactivity);
        mtextEmoji = (TextView)findViewById(R.id.text_emoji_chatactivity);
        mtextExpand = (TextView)findViewById(R.id.text_expand_chatactivity);
        mtextSend = (TextView)findViewById(R.id.text_send_chatactivity);

        mMessageLinear = (LinearLayout) findViewById(R.id.linear_editmessage_chatactivity);
        mEditMessage = (EditText) findViewById(R.id.edit_message_chatactivity);

        mimagMenu = (ImageView)findViewById(R.id.imag_menu_chatactivity);

        mRecyclerMessage = (RecyclerView) findViewById(R.id.recycler_message_chatactivity);
        //点击事件
        mtextBack.setOnClickListener(this);
        mtextVoice.setOnClickListener(this);
        mtextEmoji.setOnClickListener(this);
        mtextExpand.setOnClickListener(this);
        mimagMenu.setOnClickListener(this);
        mtextFriendname.setText(mFriendBeizhu);
        bitmapself = getpicSelf();//自己的图片
        bitmapother = getpicOther();//别人的图片
        listmyxinxi = new ArrayList<MyXinxi>();//消息列表数据初始化
        listmyxintwo = new ArrayList<MyXinxitwo>();//聊天记录初始化
        getfriendfakename();
        registerBroad();
        recoverChatData();//恢复聊天记录
    }
    //每当活动启动时，默认开启服务端，并根据好友的IP地址，开启客户端
    @Override
    protected void onResume() {
        super.onResume();
        chatClass = new ChatClass(ChatActivity.this,mEditMessage,mtextSend);
        chatClass.openServer(Constant.PORT_NUMBER);
        getipdata(chatClass);


    }
    //获得IP
    public void getipdata(final ChatClass chatClass){
        Call call = new SelectAction().GetFriendIP(getfriendusename());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call,IOException e) {
                Looper.prepare();
                ToastAction.startToast(ChatActivity.this,"网络出错");
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               String ipdata = response.body().string();//获得服务器返回的IP地址
                if(ipdata.equals("null")){//该用户还没注册过，至少登录的IP是空的
                    Looper.prepare();
                    ToastAction.startToast(ChatActivity.this,mFriendBeizhu+"不在线呢");
                    Looper.loop();
                }else{
                    chatClass.oPenClient(ipdata,Constant.PORT_NUMBER);
                }
            }
        });
    }

    //当点击edittext时，edittext有内容则让按钮出现，并切换背景
    public void edittextFuncton(){
        mEditMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {//在内容修改后
                String gettext = mEditMessage.getText().toString().trim();
                if(!gettext.equals("")){//有内容
                    mtextExpand.setVisibility(View.GONE);//扩展按钮不可见消失
                    mtextSend.setVisibility(View.VISIBLE);//消息按钮出现设置为可见
                    mMessageLinear.setBackground(getDrawable(R.drawable.youneirong));
                }else{
                    mtextExpand.setVisibility(View.VISIBLE);//扩展按钮不可见消失
                    mtextSend.setVisibility(View.GONE);//消息按钮出现设置为可见
                    mMessageLinear.setBackground(getDrawable(R.drawable.shuru));
                }
            }
        });
    }
    //点击消息发送按钮，事件处理
    public void sendMessageAction(){
        String messagerr = mEditMessage.getText().toString();//获得用户输入的信息
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.text_back_chatactivity://按返回键
               onBackPressed();
               break;
           case R.id.imag_menu_chatactivity://按菜单键
               ToastAction.startToast(ChatActivity.this,"菜单功能还在开发中");
               break;
           case R.id.text_voice_chatactivity://按语音键
               ToastAction.startToast(ChatActivity.this,"语音功能还在开发中");
               break;
           case R.id.text_emoji_chatactivity://按表情
               ToastAction.startToast(ChatActivity.this,"表情功能还在开发中");
               break;
           case R.id.text_expand_chatactivity://按功能扩展键
               ToastAction.startToast(ChatActivity.this,"扩展功能还在开发中");
               break;
//           case R.id.text_send_chatactivity://点击消息发送按钮
//               sendMessageAction();
//               break;
           default:
               break;
       }
    }

    //注册广播，发送消息的广播，和接收消息的广播
    public void registerBroad(){
        msendMessageBroadcast = new SendMessageBroadcast();
        mgetMessageBroadcast = new GetMessageBroadcast();
        IntentFilter intentFilter = new IntentFilter("com.ilikexy.bigwork.outMessage");
        IntentFilter intentFilter1 = new IntentFilter("com.ilikexy.bigwork.readMessage");
        registerReceiver(msendMessageBroadcast,intentFilter);
        registerReceiver(mgetMessageBroadcast,intentFilter1);
    }

    public void reflashUI(){
        SendMessRecyclerAdapter adapter = new SendMessRecyclerAdapter(listmyxinxi);
        mRecyclerMessage.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerMessage.setLayoutManager(manager);
        manager.scrollToPositionWithOffset(adapter.getItemCount() - 1, Integer.MIN_VALUE);



    }
    //获得自己的图片
    public Bitmap getpicSelf(){
        SharedPreferences sharedPreferencess = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE);
        String toto = sharedPreferencess.getString("picture", "");
        Bitmap bitmap=null;
        if (!toto.equals("")) {
            byte[] buff = Base64.decode(toto, Base64.DEFAULT);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(buff);
            Drawable drawable = Drawable.createFromStream(inputStream, "");
            if (drawable == null) {
                Toast.makeText(ChatActivity.this, "drawable为空", Toast.LENGTH_SHORT).show();

            } else {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
               return bitmap;
            }
        }
        return null;
    }
    //获得别人的图片
    public Bitmap getpicOther(){
        SharedPreferences sharedPreferencess = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE);
        String toto = sharedPreferencess.getString("contentpicture", "");
        Bitmap bitmap=null;
        if (!toto.equals("")) {
            byte[] buff = Base64.decode(toto, Base64.DEFAULT);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(buff);
            Drawable drawable = Drawable.createFromStream(inputStream, "");
            if (drawable == null) {
                Toast.makeText(ChatActivity.this, "drawable为空", Toast.LENGTH_SHORT).show();

            } else {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
                return bitmap;
            }
        }
        return null;
    }
    //获取系统时间方法
    public String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
         //获取当前时间
       Date date = new Date(System.currentTimeMillis());
        return String.valueOf(simpleDateFormat.format(date));
    }
    class SendMessageBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String mess = intent.getStringExtra("content");
            //--------------------------------------------------------------------------------------------------------------
            //---------------------------------------------------------------------------------------------------
            MyXinxi xinxi = new MyXinxi(mess,bitmapself,getDate(),
                    true);
            listmyxinxi.add(xinxi);
            MyXinxitwo xinxitwo = new MyXinxitwo(mess,getDate(),true);
            listmyxintwo.add(xinxitwo);
            mEditMessage.post(new Runnable() {
                @Override
                public void run() {
                   mEditMessage.setText("");
                }
            });
            reflashUI();
        }
    }

    class GetMessageBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String mess1 = intent.getStringExtra("content");
            MyXinxi xinxi1 = new MyXinxi(mess1,bitmapother,getDate(),
                    false);
            listmyxinxi.add(xinxi1);
            MyXinxitwo xinxitwo = new MyXinxitwo(mess1,getDate(),true);
            listmyxintwo.add(xinxitwo);
            reflashUI();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(msendMessageBroadcast);
        unregisterReceiver(mgetMessageBroadcast);

        //保存聊天记录
        saveChatData();
    }
    //保存聊天记录
    public void saveChatData(){
        //把聊天记录设置为json对象
        Gson gson = new Gson();
        String jsoner = gson.toJson(listmyxintwo);
        SharedPreferences.Editor share = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS,MODE_PRIVATE).edit();
        share.putString("chatdata",jsoner);
        share.apply();
    }
    //恢复聊天记录
    public void recoverChatData(){
        SharedPreferences share = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS,MODE_PRIVATE);
        String jsonerr = share.getString("chatdata","");
        if (!jsonerr.equals("")){//消息记录不为空
            Gson gson = new Gson();
            List<MyXinxitwo> list = gson.fromJson(jsonerr,new TypeToken<List<MyXinxitwo>>(){}.getType());
            //生成相应的数据
            for (int i=0;i<list.size();i++){
                MyXinxi xinxi =null;
                if (list.get(i).ismIsRight()){
                    xinxi = new MyXinxi(list.get(i).getmMessage(),bitmapself,list.get(i).getmStrTime(),list.get(i).ismIsRight());
                }else {
                    xinxi = new MyXinxi(list.get(i).getmMessage(),bitmapother,list.get(i).getmStrTime(),list.get(i).ismIsRight());
                }
                listmyxinxi.add(xinxi);
            }
            reflashUI();
        }
    }
}