package com.ilikexy.bigwork.entity;

import android.graphics.Bitmap;

public class MyXinxitwo {
    private String mMessage;//消息内容
    private String mStrTime;//这条消息的时间
    private boolean mIsRight;//是自己发的还是对方发的
    public MyXinxitwo(String cmMessage,String cmStrTime,boolean cmIsRight){
        mMessage = cmMessage;
        mStrTime = cmStrTime;
        mIsRight = cmIsRight;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String cmMessage) {
        mMessage = cmMessage;
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
