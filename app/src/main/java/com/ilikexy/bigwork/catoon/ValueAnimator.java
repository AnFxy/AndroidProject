package com.ilikexy.bigwork.catoon;

import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;

import com.ilikexy.bigwork.zidingyi.LoadDialog;

public class ValueAnimator {
    public static void catoonShow1(final LoadDialog wangZheLoad, float start, float end, int time){
        android.animation.ValueAnimator valueAnimator = ObjectAnimator.ofFloat(start,end);
        valueAnimator.setDuration(time);//变化总体时间
        valueAnimator.setInterpolator(new LinearInterpolator());//匀速变化
        valueAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(android.animation.ValueAnimator c_animator) {
                float currentprogress = (Float) c_animator.getAnimatedValue();
                wangZheLoad.setProgress(currentprogress%2);//调用自定义view中 设置进度的方法，
                //取余2是为了保证progress在 0----2之间变换
            }
        });
        valueAnimator.start();
    }
}
