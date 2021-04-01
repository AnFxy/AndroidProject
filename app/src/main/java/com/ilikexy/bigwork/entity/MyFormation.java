package com.ilikexy.bigwork.entity;

public class MyFormation {
    private String mTitle;
    private String mContent;
    private boolean misLine;//下方是否有分割线
    private boolean misGray;//下方是否有灰色分割区域
    public MyFormation(String c_title,String c_content,boolean c_line,boolean c_gray){
        mTitle = c_title;
        mContent = c_content;
        misLine = c_line;
        misGray = c_gray;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public boolean isMisLine() {
        return misLine;
    }

    public boolean isMisGray() {
        return misGray;
    }
}
