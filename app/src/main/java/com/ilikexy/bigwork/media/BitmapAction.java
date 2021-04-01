package com.ilikexy.bigwork.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class BitmapAction {
    private Bitmap mBitmap;
    private String mString;
    //图片转String
    public String bitmapToString(Bitmap c_bitmap){
        //用户在活动中上传的图片转换成String进行存储
        if(c_bitmap!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            c_bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();// 转为byte数组
            mString= Base64.encodeToString(bytes,Base64.DEFAULT);
            return mString;
        }
        else{
            return "";
        }
    }
    //String转图片
    public Bitmap stringToBitmap(String c_string){
        //数据库中的String类型转换成Bitmap
// 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(c_string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }
}
