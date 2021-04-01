package com.ilikexy.bigwork.constant;

import android.app.Activity;
import android.content.Context;

import com.ilikexy.bigwork.baseactivity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ActivityControler {
    public static List<BaseActivity> listofBa = new ArrayList<BaseActivity>();
    //添加活动
    public static void addActivity(BaseActivity c_baseactivity){
        listofBa.add(c_baseactivity);
    }
    //删除活动
    public static void deleteActivity(BaseActivity c_baseactivity){
        listofBa.remove(c_baseactivity);
    }
    //直接退出程序
    public static void destroyActivity(){
        for (BaseActivity act:listofBa){
            //遍历list中的activity，只要没有销毁，就将其销毁
            if(!act.isFinishing()){
                act.finish();
            }
        }
        //将list清除
        listofBa.clear();

    }
}
