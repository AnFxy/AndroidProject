package com.ilikexy.bigwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.jaeger.library.StatusBarUtil;

public class LogoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorLogo));
        }//底部导航栏颜色
        setContentView(R.layout.activity_logo);
        StatusBarUtil.setColor(LogoActivity.this, Color.BLACK,255);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //延时跳转
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BaseActivity.jump(LogoActivity.this,MainActivity.class);
            }
        },2000);

    }
}