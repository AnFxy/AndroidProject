package com.ilikexy.bigwork.entity;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyXinxi {
    private String mMessage;//消息内容
    private Bitmap mBitmap;//头像图片
    private String mStrTime;//这条消息的时间
    private boolean mIsRight;//是自己发的还是对方发的
    public MyXinxi(String cmMessage,Bitmap cmBitmap,String cmStrTime,boolean cmIsRight){
        mMessage = cmMessage;
        mBitmap = cmBitmap;

       mStrTime = cmStrTime;
        mIsRight = cmIsRight;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String cmMessage) {
        mMessage = cmMessage;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap cmBitmap) {
       mBitmap = cmBitmap;
    }

    public String getmStrTime() {
        return mStrTime;
    }

    public void setmStrTime(String cmStrTime) {
        mStrTime = cmStrTime;
    }

    public boolean ismIsRight() {
        return mIsRight;
    }

    public void setmIsRight(boolean cmIsRight) {
        mIsRight = cmIsRight;
    }
}
