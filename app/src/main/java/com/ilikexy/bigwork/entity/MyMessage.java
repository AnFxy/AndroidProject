package com.ilikexy.bigwork.entity;

import android.graphics.Bitmap;

public class MyMessage {
    private String mMessageTitle;
    private String mMessageContent;
    private String mMessageTime;
    private Bitmap mMessageTouxiang;
    public MyMessage(String c_title,String c_content,String c_time,Bitmap c_touxiang){
        this.mMessageTitle = c_title;
        this.mMessageContent = c_content;
        this.mMessageTime = c_time;
        this.mMessageTouxiang = c_touxiang;
    }

    public String getmMessageTitle() {
        return mMessageTitle;
    }

    public void setmMessageTitle(String mMessageTitle) {
        this.mMessageTitle = mMessageTitle;
    }

    public String getmMessageContent() {
        return mMessageContent;
    }

    public void setmMessageContent(String mMessageContent) {
        this.mMessageContent = mMessageContent;
    }

    public String getmMessageTime() {
        return mMessageTime;
    }

    public void setmMessageTime(String mMessageTime) {
        this.mMessageTime = mMessageTime;
    }

    public Bitmap getmMessageTouxiang() {
        return mMessageTouxiang;
    }

    public void setmMessageTouxiang(Bitmap mMessageTouxiang) {
        this.mMessageTouxiang = mMessageTouxiang;
    }
}
