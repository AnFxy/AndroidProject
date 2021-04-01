package com.ilikexy.bigwork.recyclerview;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilikexy.bigwork.MainFunctionActivity;
import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.baseactivity.ToastAction;
import com.ilikexy.bigwork.entity.MyAddFriend;
import com.ilikexy.bigwork.entity.MyContent;
import com.ilikexy.bigwork.netaction.SelectAction;
import com.ilikexy.bigwork.zidingyi.RoundedSquare;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddFriendRecyclerAdapter extends RecyclerView.Adapter<AddFriendRecyclerAdapter.MyViewHolder> {
    private List<MyAddFriend> mlistAddFriend;//联系人数据
    class MyViewHolder extends RecyclerView.ViewHolder{
        RoundedSquare imagView;
        TextView textView;
        ImageView imageViewAdd;
        public MyViewHolder(View view){
            super(view);
            imagView = (RoundedSquare)view.findViewById(R.id.imag_item_addfriend);//联系人图片
            textView = (TextView)view.findViewById(R.id.text_item_addfriend);//联系人名字
            imageViewAdd = (ImageView)view.findViewById(R.id.imag_addfriend_addfriend);//添加好友按钮
        }
    }
    public AddFriendRecyclerAdapter(List<MyAddFriend> c_list){
        mlistAddFriend = c_list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addcontent_addfriend,parent,false);
        final MyViewHolder holder = new MyViewHolder(view);
        //点击事件的处理
        holder.imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion = holder.getAdapterPosition();//获得适配器的哪一个元素
                final MyAddFriend addFriend = mlistAddFriend.get(postion);//获得相应的实体
                if (!addFriend.isAdded()){//mei添加
                    //网络操作,将好友添加进数据库中，你是我的好友，我是你的好友
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Call call = new SelectAction().addFriend(MainFunctionActivity.usename,addFriend.getMusename());
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure( Call call,  IOException e) {
                                    Looper.prepare();
                                    ToastAction.startToast(parent.getContext(),"网络出错了！");
                                    Looper.loop();
                                }
                                @Override
                                public void onResponse( Call call,Response response) throws IOException {
                                    String getdata = response.body().string();//获得从服务器插入数据的反馈
                                    if(getdata.equals("添加好友成功")){
                                        Looper.prepare();
                                        holder.imageViewAdd.post(new Runnable() {//用post更新ui
                                           @Override
                                            public void run() {
                                                holder.imageViewAdd.setImageResource(R.drawable.added);
                                                addFriend.setAdded(true);
                                            }
                                        });
                                        ToastAction.startToast(parent.getContext(),"添加好友成功！");
                                        Looper.loop();
                                    }else{//添加好友失败
                                        Looper.prepare();
                                        ToastAction.startToast(parent.getContext(),"添加好友失败！");
                                        Looper.loop();
                                    }
                                }
                            });

                        }
                    }).start();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //列表数据绑定
        MyAddFriend addFriend = mlistAddFriend.get(position);
        holder.textView.setText(addFriend.getMfakename());
        if (addFriend.isAdded()){
            holder.imageViewAdd.setImageResource(R.drawable.added);
        }else{
            holder.imageViewAdd.setImageResource(R.drawable.tianjia);
        }
    }

    @Override
    public int getItemCount() {
        return mlistAddFriend.size();
    }
}
