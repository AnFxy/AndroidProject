package com.ilikexy.bigwork.chat;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ilikexy.bigwork.baseactivity.ToastAction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatClass {
    Context mContext;//全局上下文
    EditText mEditText;//输入框
    TextView mTextView;//点击发送按钮
    public  Thread serverThread;
    public  Thread clientThread;

    public ChatClass(Context context,EditText editText,TextView textView){//构造函数
        mContext = context;
        mEditText = editText;
        mTextView  = textView;
    }
    //消息线程，
    public  void oPenMessage(final Socket socket){//需要socket对象

        try {
            final OutputStream out = socket.getOutputStream();//获得socket输出流
                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//当点击发送消息时，
                        if (socket!=null) {
                            final String inputdata = mEditText.getText().toString();//获得控件上的输入内容，无需判断内容是否为空
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        out.write(inputdata.getBytes());//发送消息
                                        out.flush();//清空缓存
                                        Intent intent = new Intent("com.ilikexy.bigwork.outMessage");//更新ui
                                        intent.putExtra("content", inputdata);
                                        mContext.sendBroadcast(intent);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }else{
                            ToastAction.startToast(mContext,"socket被销毁");
                        }
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    //开启客户端
    public  void oPenClient(final String serverIP, final int serverPort){
        //开启客户端线程

        clientThread =  new Thread(new Runnable() {
           Socket socket;
            @Override
            public void run() {
                try {
                     socket = new Socket(serverIP,serverPort);//服务端的IP和端口号
                    oPenMessage(socket);//开启聊天线程，客户端可向服务器发送消息
                    InputStream in = socket.getInputStream();//客户端读取服务器发回来的消息
                    int DataLength = 0;//数据长度
                    byte[] bytes = new byte[1024];//读取1k的量
                    while ((DataLength = in.read(bytes))!=-1){//只要服务器端发来消息
                        String getdata = new String(bytes,0,DataLength);//接收到这条消息
                        Intent intent1 = new Intent("com.ilikexy.bigwork.readMessage");
                        intent1.putExtra("content",getdata);
                        mContext.sendBroadcast(intent1);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
       clientThread.start();

    }
    //开启服务端
    public void openServer(final int serverPort){
        //开启服务端
        serverThread = new Thread(new Runnable() {
            ServerSocket serverSocket;
            Socket socket;
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(serverPort);//服务器端的serversocket
                    socket = serverSocket.accept();//Tcp的第三次握手
                    oPenMessage(socket);//开启消息线程，发送消息给客户端
                    InputStream in = socket.getInputStream();//客户端读取服务器发回来的消息
                    int DataLength = 0;//数据长度
                    byte[] bytes = new byte[1024];//读取1k的量
                    while ((DataLength = in.read(bytes))!=-1){//只要服务器端发来消息
                        String getdata = new String(bytes,0,DataLength);//接收到这条消息
                        Intent intent1 = new Intent("com.ilikexy.bigwork.readMessage");
                        intent1.putExtra("content",getdata);
                        mContext.sendBroadcast(intent1);

                    }//接收来自客户端的消息

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        serverThread.start();
    }

}
