package com.ilikexy.bigwork.baseactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.constant.ActivityControler;
import com.jaeger.library.StatusBarUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorAccent));
        }//底部导航栏颜色
        ActivityControler.addActivity(this);
       // StatusBarUtil.setColor(this,Color.BLACK);//设置状态栏背景色
       // StatusBarUtil.setDarkMode(this);
    }
    //常用启动活动的函数
    public static void jump(Context context,Class cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityControler.deleteActivity(this);
    }
}
