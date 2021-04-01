package com.ilikexy.bigwork.recyclerview;

import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.entity.MyFound;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoundRecyclerAdapter extends RecyclerView.Adapter<FoundRecyclerAdapter.MyViewHolder> {
    private List<MyFound> mlistofFound;

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView holdImageview;
        TextView holdTextView;
        TextView holdTextView2;
        View holdGrayView;
        LinearLayout holdLinear;
        View holdParentView;
        public MyViewHolder(View view){
            super(view);
            holdImageview = (ImageView)view.findViewById(R.id.imag_item_found);
            holdTextView = (TextView)view.findViewById(R.id.text_item_found);
            holdTextView2 = (TextView)view.findViewById(R.id.text_hint_item_found);
            holdGrayView = (View)view.findViewById(R.id.view_gray_itemfound);
            holdLinear = (LinearLayout)view.findViewById(R.id.linear_line_itemfound);
            holdParentView = view;
        }
    }

    public FoundRecyclerAdapter(List<MyFound> c_mlistoffound){
        mlistofFound = c_mlistoffound;
    }


    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_found_recycler,parent,false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        //列表点击事件处理
        myViewHolder.holdParentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = myViewHolder.getAdapterPosition();
                MyFound myFound = mlistofFound.get(position);
                Toast.makeText(parent.getContext(),""+myFound.getmFuncText()+"  功能还在开发中",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        MyFound myFound = mlistofFound.get(position);
        holder.holdImageview.setImageBitmap(myFound.getmBitmap());
        holder.holdTextView.setText(myFound.getmFuncText());
        holder.holdTextView2.setText(myFound.getmFuncHint());
        if (myFound.getMisGray()){//下方灰色分割区域可见
            holder.holdGrayView.setVisibility(View.VISIBLE);
        }else{//不可见
            holder.holdGrayView.setVisibility(View.GONE);
        }
        if (myFound.getMisLine()){//下方灰色分割线可见
            holder.holdLinear.setVisibility(View.VISIBLE);
        }else{//不可见
            holder.holdLinear.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mlistofFound.size();
    }
}
