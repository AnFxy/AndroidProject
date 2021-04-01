package com.ilikexy.bigwork.photouri;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class PicCutAndScale {

    //图片裁剪并缩放
    public static Bitmap getSquareRect(Bitmap c_bitmap,int x_xiangsu){
        int widthbitmap = c_bitmap.getWidth();
        int heightbitmap = c_bitmap.getHeight();
        int shorter = widthbitmap>heightbitmap?heightbitmap:widthbitmap;//取最短的
        Bitmap newBitmap;
        if (shorter==widthbitmap){//高度更长,如手机竖直拍照
            newBitmap = Bitmap.createBitmap(c_bitmap,0,(heightbitmap-widthbitmap)/2,shorter,shorter);
        }else{//宽度更长，如横向拍照
            newBitmap = Bitmap.createBitmap(c_bitmap,(widthbitmap-heightbitmap)/2,0,shorter,shorter);
        }
        //将图片进行缩放
        float rate = (float) ((float) x_xiangsu/(float) shorter);
        Matrix matrix = new Matrix();
        matrix.postScale(rate,rate);
        newBitmap = Bitmap.createBitmap(newBitmap,0,0,shorter,shorter,matrix,true);
        return newBitmap;
    }
    //不缩放
    public static Bitmap getSquareRect(Bitmap c_bitmap){
        int widthbitmap = c_bitmap.getWidth();
        int heightbitmap = c_bitmap.getHeight();
        int shorter = widthbitmap>heightbitmap?heightbitmap:widthbitmap;//取最短的
        Bitmap newBitmap;
        if (shorter==widthbitmap){//高度更长,如手机竖直拍照
            newBitmap = Bitmap.createBitmap(c_bitmap,0,(heightbitmap-widthbitmap)/2,shorter,shorter);
        }else{//宽度更长，如横向拍照
            newBitmap = Bitmap.createBitmap(c_bitmap,(widthbitmap-heightbitmap)/2,0,shorter,shorter);
        }

        return newBitmap;
    }
}
