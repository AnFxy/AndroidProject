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

public class FormaRecyclerAdapter extends RecyclerView.Adapter<FormaRecyclerAdapter.MyViewHolder> {
    private List<MyFormation> myFormationList;
    public FormaRecyclerAdapter(List<MyFormation> c_list){
        myFormationList = c_list;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle,textContent;//需要绑定id进行相关的数据填充
        View FormationView;//同时，点击之后会跳转到属性修改活动，这个活动的控件设置可见性，减少活动冗余问题
        LinearLayout linearGray;//灰色区域
        View viewLine;//灰色线
        public MyViewHolder(View view){
            super(view);
            FormationView = view;
            textTitle = (TextView)view.findViewById(R.id.text_title_itemforma);
            textContent = (TextView)view.findViewById(R.id.text_content_itemforma);
            linearGray = (LinearLayout)view.findViewById(R.id.view_gray_itemforma);
            viewLine = (View)view.findViewById(R.id.view_line_itemforma);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_formation_recycler,parent,
                false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        //点击item后会跳转到信息修改界面，信息界面的控件可视化参数由此传入
        viewHolder.FormationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                MyFormation myFormation1 = myFormationList.get(position);
                //分类讨论处理放在活动中，这里只管传参数
                Intent intent = new Intent();
                intent.putExtra("name",myFormation1.getmTitle());//要显示哪一个控件
                intent.putExtra("data",myFormation1.getmContent());//控件初始值是多少
                intent.setClass(parent.getContext(), FormationChangeActivity.class);
                parent.getContext().startActivity(intent);//跳转
            }
        });
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
