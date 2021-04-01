package com.ilikexy.bigwork.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.entity.MyMessage;
import com.ilikexy.bigwork.zidingyi.RoundedSquare;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MessRecyclerAdapter extends RecyclerView.Adapter<MessRecyclerAdapter.MyViewHolder>{
    private List<MyMessage> mListofMessage;
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView messageTitle, messageContent,messageTime;
        RoundedSquare messageImg;
        public MyViewHolder(View view){
            super(view);
            messageTitle = (TextView)view.findViewById(R.id.text_meitem_title);
            messageContent = (TextView)view.findViewById(R.id.text_meitem_text);
            messageTime = (TextView)view.findViewById(R.id.text_meitem_time);
            messageImg = (RoundedSquare) view.findViewById(R.id.imag_message_touxiang);
        }
    }
    public MessRecyclerAdapter(List<MyMessage> c_list){
        mListofMessage = c_list;
    }
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //item中的上下文是从父类控件Fragment(recyclerview)中获取, Fragment本身不是上下文，它也需要从活动中获取
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_recycler,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyMessage myMessage = mListofMessage.get(position);
        holder.messageTitle.setText(myMessage.getmMessageTitle());
        holder.messageContent.setText(myMessage.getmMessageContent());
        holder.messageTime.setText(myMessage.getmMessageTime());
        holder.messageImg.setImageBitmap(myMessage.getmMessageTouxiang());
    }

    @Override
    public int getItemCount() {
        return mListofMessage.size();
    }



}
