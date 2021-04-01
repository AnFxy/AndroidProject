package com.ilikexy.bigwork;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.constant.Constant;
import com.ilikexy.bigwork.netaction.GetIPClass;
import com.ilikexy.bigwork.zidingyi.DialogContainer;
import com.jaeger.library.StatusBarUtil;

import java.io.IOException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView text_back_loginactivity,text_nofunction_one,text_nofunction_two;
    private Button btn_login_loginactivity;
    private EditText edit_zhanghao,edit_mima;
    private View view_line_zhanghao,view_line_mima;
    private boolean isCanLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setColor(LoginActivity.this, Color.parseColor("#ededed"),0);
        StatusBarUtil.setLightMode(LoginActivity.this);
        init();
        //从注册那里传来的的账号和密码的登记
        recoverUsePass();
    }
    //从注册那里传来的的账号和密码的登记
    public void recoverUsePass(){
        SharedPreferences sp = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS,MODE_PRIVATE);
        String getusenamer = sp.getString("usename","");
        String getpassworder = sp.getString("password","");
        if (!getusenamer.isEmpty()&&!getpassworder.isEmpty()){
            edit_zhanghao.setText(getusenamer);
            edit_mima.setText(getpassworder);
        }
    }
    //整体初始化
    public void init(){
        kongjianInit();//控件的初始化
        //只有当两个edittext都有数据时，点击登录的按钮才会变为绿色，否则为灰色且无法点击
        //实现思路是在两个edittext有内容输入时进行检测
        edit_zhanghao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                jianceEditText();
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        edit_mima.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {jianceEditText();}
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    //封装一下edittext控件输入时的检测事件
    public void jianceEditText(){
        String getusename = edit_zhanghao.getText().toString().trim();
        String getpassword = edit_mima.getText().toString().trim();
        if (!getusename.equals("")&&!getpassword.equals("")){//只有当两个edittext都有数据时
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isCanLogin = true;
                    btn_login_loginactivity.setBackground(getDrawable(R.drawable.button_selector3));
                    btn_login_loginactivity.setTextColor(Color.parseColor("#ffffff"));

                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isCanLogin = false;
                    btn_login_loginactivity.setBackground(getDrawable(R.drawable.button_selector31));
                    btn_login_loginactivity.setTextColor(Color.parseColor("#b1b1b1"));

                }
            });
        }
    }
    
    //控件的初始化
    public void kongjianInit(){
        text_back_loginactivity = (TextView)findViewById(R.id.text_back_loginactivity);
        text_nofunction_one = (TextView)findViewById(R.id.text_nofunction_one);
        text_nofunction_two = (TextView)findViewById(R.id.text_nofunction_two);
        edit_zhanghao = (EditText)findViewById(R.id.edit_zhanghao_loginactivity);
        edit_mima = (EditText)findViewById(R.id.edit_mima_loginactivity);
        btn_login_loginactivity = (Button) findViewById(R.id.btn_login_loginactivity);
        //这些控件都有点击事件
        text_back_loginactivity.setOnClickListener(this);
        text_nofunction_one.setOnClickListener(this);
        text_nofunction_two.setOnClickListener(this);
        edit_zhanghao.setOnClickListener(this);
        edit_mima.setOnClickListener(this);
        btn_login_loginactivity.setOnClickListener(this);
        //此外，两条线需要有背景更换
        view_line_zhanghao = (View)findViewById(R.id.line_zhanghaocheck_login);
        view_line_mima = (View)findViewById(R.id.line_mimacheck_login);
    }


    //对于点击事件分类处理处理
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_back_loginactivity:
                this.onBackPressed();//或者是finish
                break;
            case R.id.text_nofunction_one:
                Toast.makeText(LoginActivity.this,"别点了，没打算实现这个功能",Toast.LENGTH_SHORT).show();
                break;
            case R.id.text_nofunction_two:
                Toast.makeText(LoginActivity.this,"别点了，没打算实现这个功能",Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_zhanghao_loginactivity:
                clickfunction(edit_zhanghao,edit_mima,Color.parseColor("#04c35c"),
                        Color.parseColor("#ececec"));
                break;
            case R.id.edit_mima_loginactivity:
                clickfunction(edit_mima,edit_zhanghao,Color.parseColor("#ececec"),
                        Color.parseColor("#04c35c"));
                break;
            case R.id.btn_login_loginactivity:
                if (isCanLogin){
                    DialogContainer.showDialog(LoginActivity.this,R.layout.dialog_login,R.id.load_zidiyiview);//调用弹窗方法
                    checkUsenamePassword();
                }else{
                    Toast.makeText(LoginActivity.this,"账号或密码不能为空，大哥~",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //封装一下点击edittext事件的方法，不然代码冗余
    public void clickfunction(EditText edit1,EditText edit2,int color1,int color2){
        edit1.setFocusable(true);
        edit1.setFocusableInTouchMode(true);
        edit2.setFocusableInTouchMode(false);
        edit1.requestFocus();
        edit1.requestFocusFromTouch();
        view_line_zhanghao.setBackgroundColor(color1);
        view_line_mima.setBackgroundColor(color2);//弹出键盘
        InputMethodManager inputManager = (InputMethodManager)
                edit1.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit1, 0);
    }

    //封装 用户登录事件的处理，访问网络服务器，验证账号密码是否正确
    public void checkUsenamePassword(){
        //访问服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String getusenamer = edit_zhanghao.getText().toString().trim();//获取账号和密码
                String getpassword = edit_mima.getText().toString().trim();
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+"Login?usename="+getusenamer+"&password="+getpassword+"&ipaddress="+getip())
                        .build();
                //建立Call
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) { //请求失败，没有网络，无法连接服务器
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this,"无法连接服务器，请检查网络",Toast.LENGTH_SHORT).show();
                        //销毁弹窗
                        DialogContainer.dialog.setCancelable(true);
                        DialogContainer.dialog.cancel();
                        Looper.loop();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();//请求结果进行判断
                        if (responseData.equals("login successful !!!!")){
                            Looper.prepare();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                            BaseActivity.jump(LoginActivity.this,MainFunctionActivity.class);
                            SharedPreferences.Editor share = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS,MODE_PRIVATE).edit();
                            share.putString("usename",getusenamer);
                            share.commit();
                            Looper.loop();

                        }else{
                            Looper.prepare();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this,"登录失败！",Toast.LENGTH_SHORT).show();
                            //销毁弹窗
                            DialogContainer.dialog.setCancelable(true);
                            DialogContainer.dialog.cancel();
                            Looper.loop();
                        }
                    }
                });
            }
        }).start();
    }

    //登录时，把自己的IP地址发到服务器
    public String getip(){
        String iper = GetIPClass.getClientIp(LoginActivity.this);
        Log.d("ip",iper);
        if(!iper.equals("error")&&!iper.equals("127.0.0.1")){//只要获取的IP地址无误
            return iper;
        }else{
            return "";
        }
    }

}//类