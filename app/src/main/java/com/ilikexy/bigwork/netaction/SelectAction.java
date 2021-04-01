package com.ilikexy.bigwork.netaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ilikexy.bigwork.LoginActivity;
import com.ilikexy.bigwork.LogoActivity;
import com.ilikexy.bigwork.MainActivity;
import com.ilikexy.bigwork.RegisterActivity;
import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.constant.Constant;
import com.ilikexy.bigwork.media.BitmapAction;
import com.ilikexy.bigwork.zidingyi.DialogContainer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelectAction{
    //查询单向字段数据，比如昵称，账号，密码等，支持任意类型
    public  Call selectFormation(String c_usename,String c_column){

                //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                                "Select?usename="+c_usename+"&"+c_column+"=yy")
                        .build();
                //发送请求
                Call call = client.newCall(request);
                return call;
    }
    //查询全部类型的数据
    public  Call selectFormation(String c_usename){

        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "SelectAll?usename="+c_usename)
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
    //更新单个数据
    public  Call updateFormation(String c_usename,String c_titler,String c_data){

        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "Update?usename="+c_usename+"&"+c_titler+"="+c_data+"")
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
    //查询可添加好友
    public Call selectCanAddFriend(String c_usename){
        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "SelectCanAddFriend?usename="+c_usename)
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
    //添加好友操作，需要本人的usename,和好友的usename
    public Call addFriend(String c_usename,String c_friendusename){
        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "AddFriend?usename="+c_usename+"&friendusename="+c_friendusename)
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
    //查询已有好友操作，需要本人的usename,
    public Call selectFriend(String c_usename){
        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "SelectFriend?usename="+c_usename)
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
    //修改好友备注，需要本人的usename,需要好友的 fakename,需要修改的备注名
    public Call changeBeizhu(String c_usename,String c_friendusename,String c_beizhu){
        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "UpdateBeizhu?usename="+c_usename+"&friendusename="+c_friendusename+"&friendbeizhuname="+c_beizhu)
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
    //查询指定方信号的用户，并查询是否已经是自己好友
    public Call SelectFangxin(String c_usename,String c_fangxinusename){
        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "SelectFangxin?usename="+c_usename+"&fangxinusename="+c_fangxinusename)
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
    //删除指定用户，需要自己的usename, 需要对方的 friendusename,删除usecontent中，两条数据
    public Call DeleteFriend(String c_usename,String c_friendusename){
        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "DeleteFriend?usename="+c_usename+"&friendusename="+c_friendusename)
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
    //获取好友的ip地址，
    public Call GetFriendIP(String c_friendusename){
        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "GetIP?friendusename="+c_friendusename)
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
    //获取好友的 备注
    public Call SelectFriendBeizhu(String c_usename,String c_friendusename){
        //堵塞，网络有结果后，再返回数据，毕竟也不用那么慢
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.STRING_SERVICE_URL+Constant.STRING_SERVICE_PROJECTNAME+
                        "SelectFriendBeizhu?usename="+c_usename+"&friendusename="+c_friendusename)
                .build();
        //发送请求
        Call call = client.newCall(request);
        return call;
    }
}
