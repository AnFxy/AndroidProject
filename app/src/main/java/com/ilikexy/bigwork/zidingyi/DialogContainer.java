package com.ilikexy.bigwork.zidingyi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.baseactivity.ToastAction;
import com.ilikexy.bigwork.catoon.ValueAnimator;

public class DialogContainer{
    public static Dialog dialog;
    public static void showDialog(Context context,int c_dialogid,int kongjianid){
         dialog = new Dialog(context, R.style.JumpDialog);//指明Dialog容器弹出的动画风格
        //根据layout文件绘制出加载动画的视图
        LinearLayout linear = (LinearLayout) LayoutInflater.from(context).inflate(c_dialogid,null);
        LoadDialog loadDialog = (LoadDialog)linear.findViewById(kongjianid);//绑定id
        ValueAnimator.catoonShow1(loadDialog,0,8,8000);
        dialog.setContentView(linear);//将视图加入容器
        Window dialogWindow = dialog.getWindow();//获得窗口
        dialogWindow.setGravity(Gravity.CENTER);//放置在底部
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int)context.getResources().getDisplayMetrics().widthPixels; // 宽度
        linear.measure(0, 0);
        lp.height = linear.getMeasuredHeight();
        lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }
    public static void showPictureDialog(final Context context, int c_dialogid, final String zhaoxiang_action, final String dcim_action){
        dialog = new Dialog(context, R.style.JumpDialog);//指明Dialog容器弹出的动画风格
        //根据layout文件绘制出加载动画的视图
        LinearLayout linear = (LinearLayout) LayoutInflater.from(context).inflate(c_dialogid,null);
        TextView kongjian1 = (TextView)linear.findViewById(R.id.text_camera);
        TextView kongjian2 = (TextView)linear.findViewById(R.id.text_dcim);
        TextView kongjian3 = (TextView)linear.findViewById(R.id.text_cancel);
        kongjian1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送打开相机通知
                Intent intent = new Intent(zhaoxiang_action);
                context.sendBroadcast(intent);
                deleteDialog();
            }
        });
        kongjian2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(dcim_action);
                context.sendBroadcast(intent1);
                deleteDialog();
            }
        });
        kongjian3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 deleteDialog();
            }
        });
        dialog.setContentView(linear);//将视图加入容器
        Window dialogWindow = dialog.getWindow();//获得窗口
        dialogWindow.setGravity(Gravity.BOTTOM);//放置在底部
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int)context.getResources().getDisplayMetrics().widthPixels; // 宽度
        linear.measure(0, 0);
        lp.height = linear.getMeasuredHeight();
        lp.alpha = 1; // 透明度
        dialogWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }

    //联系人菜单操作
    public static void showContentDialog(final Context context, final TextView c_beizhu){
        dialog = new Dialog(context, R.style.JumpDialog);//指明Dialog容器弹出的动画风格
        LinearLayout linear = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dialog_content,null);
        TextView beizhu = (TextView)linear.findViewById(R.id.text_beizhu);//备注好友
        TextView blackname = (TextView)linear.findViewById(R.id.text_blackname);//加入黑名单
        TextView deletefriend = (TextView)linear.findViewById(R.id.text_deletefriend);//删除好友
        TextView cancelchose = (TextView)linear.findViewById(R.id.text_content_cancel);//取消
        //点击事件的处理
        beizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                //弹出修改备注的对话框
                final EditText editText = new EditText(context);//动态生成edittextview
                editText.setPadding(80,0,0,0);
                editText.setTextSize(16);
                new AlertDialog.Builder(context).setTitle("修改备注")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String textget = editText.getText().toString();//获得用户输入的备注内容
                                if (textget.equals("")){//备注为空不允许
                                    ToastAction.startToast(context,"备注不可为空");
                                }else{
                                    //发送修改备注的广播
                                    Intent intent = new Intent("com.ilikexy.bigwork.changebeizhu");
                                    intent.putExtra("beizhu",textget);//备注名
                                    context.sendBroadcast(intent);
                                }
                            }
                        })
                        .setNegativeButton("取消",null).show();
            }
        });

        blackname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastAction.startToast(context,"黑名单功能还在开发中");
                dialog.cancel();
            }
        });
        //删除好友
        deletefriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                //发送删除好友的广播
                Intent intent = new Intent("com.ilikexy.bigwork.deletefriend");
                context.sendBroadcast(intent);
            }
        });
        cancelchose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.setContentView(linear);//将视图加入容器
        Window dialogWindow = dialog.getWindow();//获得窗口
        dialogWindow.setGravity(Gravity.BOTTOM);//放置在底部
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int)context.getResources().getDisplayMetrics().widthPixels; // 宽度
        linear.measure(0, 0);
        lp.height = linear.getMeasuredHeight();
        lp.alpha = 1; // 透明度
        dialogWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();

    }
    public static void deleteDialog(){
        if (dialog!=null){
            dialog.cancel();
        }
    }
}
