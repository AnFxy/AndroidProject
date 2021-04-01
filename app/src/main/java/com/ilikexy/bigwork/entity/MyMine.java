package com.ilikexy.bigwork.entity;

import android.graphics.Bitmap;

public class MyMine {
    private Bitmap mBitmap;
    private String mFakeName;
    private String mUseName;
    public MyMine(Bitmap c_bitmap,String c_fakename,String c_usename){
        mBitmap = c_bitmap;
        mFakeName = c_fakename;
        mUseName = c_usename;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public String getmFakeName() {
        return mFakeName;
    }

    public String getmUseName() {
        return mUseName;
    }
}
