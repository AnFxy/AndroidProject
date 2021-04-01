package com.ilikexy.bigwork.entity;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyFound {
    private Bitmap mBitmap;//朋友圈图片
    private String mFuncText;//朋友圈文字
    private String mFuncHint;//朋友圈提示
    private boolean misLine;//下方是否有分割线
    private boolean misGray;//下方是否有灰色分割区域
    public MyFound(Bitmap c_bitmap,String c_text,String c_hint,boolean c_line,boolean c_isgray){
        mBitmap = c_bitmap;
        mFuncText = c_text;
        mFuncHint = c_hint;
        misLine = c_line;
        misGray = c_isgray;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }



    public String getmFuncText() {
        return mFuncText;
    }
    public String getmFuncHint() {
        return mFuncHint;
    }

    public boolean getMisGray() {
        return misGray;
    }
    public boolean getMisLine() {
        return misLine;
    }
}
