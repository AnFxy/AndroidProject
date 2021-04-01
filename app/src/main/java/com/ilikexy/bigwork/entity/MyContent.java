package com.ilikexy.bigwork.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MyContent implements Serializable {
    private Bitmap mbitmap;//联系人图片
    private String mfriendname;//联系人备注
    private boolean mline;//下划线
    private boolean mgray;//灰色分割区域

    private String mUsename;//联系人的账号
    public MyContent(Bitmap c_bitmap,String c_name,String c_usename,boolean c_line,boolean c_gray){
        mbitmap = c_bitmap;
        mfriendname = c_name;
        mline = c_line;
        mgray = c_gray;
        mUsename = c_usename;
    }

    public Bitmap getMbitmap() {
        return mbitmap;
    }

    public String getMfriendname() {
        return mfriendname;
    }

    public boolean isMline() {
        return mline;
    }

    public boolean isMgray() {
        return mgray;
    }

    public String getmUsename() {
        return mUsename;
    }

    public void setmUsename(String mUsename) {
        this.mUsename = mUsename;
    }
}
