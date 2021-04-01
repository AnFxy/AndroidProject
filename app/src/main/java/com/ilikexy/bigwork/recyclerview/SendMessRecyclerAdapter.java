package com.ilikexy.bigwork.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.entity.MyXinxi;
import com.ilikexy.bigwork.zidingyi.RoundedSquare;

import org.w3c.dom.Text;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SendMessRecyclerAdapter extends RecyclerView.Adapter<SendMessRecyclerAdapter.MyViewHolder> {
    private List<MyXinxi> mListXinxi;

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView one,two;
        LinearLayout linearleft;
        LinearLayout linearright;
        TextView textTime;
        RoundedSquare touxiangleft;
        RoundedSquare touxiangright;
        public MyViewHolder(View view){
            super(view);
            //控件的绑定
            one =(TextView)view.findViewById(R.id.text_one);
            two = (TextView)view.findViewById(R.id.text_two);
            linearleft = (LinearLayout)view.findViewById(R.id.linear_left);
            linearright = (LinearLayout)view.findViewById(R.id.linear_right);
            textTime = (TextView)view.findViewById(R.id.text_time);
            touxiangleft = (RoundedSquare)view.findViewById(R.id.imag_left);
            touxiangright = (RoundedSquare)view.findViewById(R.id.imag_right);
        }
    }
    public SendMessRecyclerAdapter(List<MyXinxi> c_mListXinxi){
        mListXinxi = c_mListXinxi;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ccc,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MyXinxi xinxi = mListXinxi.get(position);
        holder.textTime.setText(xinxi.getmStrTime());
        if (xinxi.ismIsRight()){//是否是自己发的
            holder.two.setText(xinxi.getmMessage());
            holder.touxiangright.setMbitmap(xinxi.getmBitmap());
            holder.linearleft.setVisibility(View.GONE);//左侧不可见
        }else{
            holder.one.setText(xinxi.getmMessage());
            holder.touxiangleft.setMbitmap(xinxi.getmBitmap());
            holder.linearright.setVisibility(View.GONE);//右侧不可见
        }

    }

    @Override
    public int getItemCount() {
        return mListXinxi.size();
    }


}
