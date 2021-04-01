package com.ilikexy.bigwork.zidingyi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.photouri.PicCutAndScale;

public class RoundedSquare extends androidx.appcompat.widget.AppCompatImageView {
    //需要裁剪的图片是个默认头像
    public Bitmap mbitmap = BitmapFactory.decodeResource(getResources(),R.drawable.graytouxiang);
    public static int warpContentSize=45;
    public int mroudedSquareRadius =3;
    public Paint mroudedSquarePaint;
    public RoundedSquare(Context context){
        this(context,null);
    }
    public RoundedSquare(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public RoundedSquare(Context context, AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        initKongjian(context,attrs);
    }
    //sp转dp函数
    //sp  to  px  不多说，就是 sp转化为 px,要是控件使用者用dp怎么办，那就让他自己添加个dp转sp的函数，就你事多
    public int sp2px(int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, this.getResources().getDisplayMetrics());
    }
    //控件属性初始化
    public void initKongjian(Context context,AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundedSquare);
        mroudedSquareRadius = array.getDimensionPixelSize(R.styleable.RoundedSquare_roundedRadius,sp2px(mroudedSquareRadius));
        mroudedSquarePaint = new Paint();//画笔初始化
        mroudedSquarePaint.setAntiAlias(true);//画笔抗锯齿等
        mroudedSquarePaint.setDither(true);
        //默认从控件设置中放置图片，要是控件没设置图片则用默认图片
        if (getDrawable()!=null){
            mbitmap = ((BitmapDrawable)getDrawable()).getBitmap();//从控件中获取
        }
        //当然，可能图片是在服务器中获取的，所以还要有外界设置图片
    }
    //测量控件的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //我有很多话要说，趁我还是清醒装态
        //当用户使用wrap_content时，默认是45sp的正方形，控件的长度就是图片的长度45sp
        //当用户加入padding属性后，图片就会距离控件一定宽度，所以我们的widthSize = 图片长度 + paddingleft+paddingright
        //然后在canvas绘制路径时，这个圆角正方形开始位置X坐标应该是 paddingleft, Y坐标应该是paddingTop
        //圆角正方形长度就是图片长度，所以结束X坐标应该要是 图片长度+paddingleft =  控件宽度 - paddingright
        //根据路径绘制图片时，图片起始就是，getpaddingleft,和getpaddingtop
        if (widthMode==MeasureSpec.AT_MOST||heightMode==MeasureSpec.AT_MOST){//wrap_content
            widthSize = sp2px(warpContentSize)+getPaddingLeft()+getPaddingRight();
            heightSize = sp2px(warpContentSize)+getPaddingBottom()+getPaddingTop();
        }
        setMeasuredDimension(widthSize,heightSize);
    }
    //绘制圆角路径
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        Path path = new Path();
        RectF rectF = new RectF(getPaddingLeft(),getPaddingTop(),getWidth()-getPaddingRight(),getHeight()-getPaddingBottom());
        //矩形大小，横向半径，纵向半径，绘制方向
        path.addRoundRect(rectF,mroudedSquareRadius,mroudedSquareRadius, Path.Direction.CW);
        canvas.clipPath(path);//裁剪区域
        //图片预处理,移动图片位置，将宽高设置为相同，进行裁剪，后缩放为系统要求的大小
        //1.移动图片图片裁剪为正方形。,...我觉得这不应该每次写都要进行处理，所以我把它封装成一个方法
        Bitmap newBitmap = PicCutAndScale.getSquareRect(mbitmap,getWidth()-getPaddingLeft()-getPaddingTop());
        //图片已经移动裁剪好了，接下来就是根据路径，对图片进行框型裁剪
        canvas.drawBitmap(newBitmap,getPaddingLeft(),getPaddingTop(),mroudedSquarePaint);
        canvas.restore();
    }

    //外界设置图片
    public void setMbitmap(Bitmap c_bitmap){
        mbitmap = c_bitmap;
        invalidate();
    }
   //外界调用父类设置图片的方法时，也需要重新绘制
    @Override
    public void setImageBitmap(Bitmap bm) {
        //super.setImageBitmap(bm);
        mbitmap = bm;
        invalidate();
    }

    public Bitmap getMbitmap(){
        return mbitmap;
    }
}
