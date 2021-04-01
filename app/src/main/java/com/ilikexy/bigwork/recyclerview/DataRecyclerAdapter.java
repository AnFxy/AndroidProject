package com.ilikexy.bigwork.recyclerview;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikexy.bigwork.FormationChangeActivity;
import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.entity.MyFormation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DataRecyclerAdapter extends RecyclerView.Adapter<DataRecyclerAdapter.MyViewHolder> {
    private List<MyFormation> myFormationList;
    public DataRecyclerAdapter(List<MyFormation> c_list){
        myFormationList = c_list;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle,textContent;//需要绑定id进行相关的数据填充
        LinearLayout linearGray;//灰色区域
        View viewLine;//灰色线
        public MyViewHolder(View view){
            super(view);
            textTitle = (TextView)view.findViewById(R.id.text_title_itemdata);
            textContent = (TextView)view.findViewById(R.id.text_content_itemdata);
            linearGray = (LinearLayout)view.findViewById(R.id.view_gray_itemdata);
            viewLine = (View)view.findViewById(R.id.view_line_itemdata);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_recycler,parent,
                false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyFormation myFormation = myFormationList.get(position);
        holder.textTitle.setText(myFormation.getmTitle());
        holder.textContent.setText(myFormation.getmContent());
        if (myFormation.isMisLine()){
            holder.viewLine.setVisibility(View.VISIBLE);
        }else{
            holder.viewLine.setVisibility(View.GONE);
        }
        if (myFormation.isMisGray()){
            holder.linearGray.setVisibility(View.VISIBLE);
        }else {
            holder.linearGray.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return myFormationList.size();
    }






}
