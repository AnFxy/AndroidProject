package com.ilikexy.bigwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.jaeger.library.StatusBarUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_login_mainactivity,btn_register_mainacitivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorLogo));
        }//底部导航栏颜色
        setContentView(R.layout.activity_main);
        StatusBarUtil.setColor(MainActivity.this, Color.BLACK);//设置状态栏背景色
        init();
    }
    public void init(){
        btn_login_mainactivity = (Button)findViewById(R.id.btn_login_mainacitvity);
        btn_register_mainacitivity = (Button)findViewById(R.id.btn_register_mainacitvity);
        btn_register_mainacitivity.setOnClickListener(this);
        btn_login_mainactivity.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //延时跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               btn_login_mainactivity.setVisibility(View.VISIBLE);
               btn_register_mainacitivity.setVisibility(View.VISIBLE);
            }
        },1500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_mainacitvity:
                BaseActivity.jump(MainActivity.this,LoginActivity.class);
                break;
            case R.id.btn_register_mainacitvity:
                BaseActivity.jump(MainActivity.this,RegisterActivity.class);
                break;
            default:
                break;
        }
    }
}