package com.ilikexy.bigwork.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikexy.bigwork.DataActivity;
import com.ilikexy.bigwork.MainFunctionActivity;
import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.constant.Constant;
import com.ilikexy.bigwork.entity.MyContent;
import com.ilikexy.bigwork.zidingyi.RoundedSquare;

import java.io.ByteArrayOutputStream;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.MyViewHolder> {
    private List<MyContent> mlistContent;//联系人数据
    class MyViewHolder extends RecyclerView.ViewHolder{
        View contentView;
        RoundedSquare imagView;
        TextView textView;
        LinearLayout line;
        LinearLayout gray;
        public MyViewHolder(View view){
            super(view);
            contentView = view;//联系人列表item
            imagView = (RoundedSquare)view.findViewById(R.id.imag_item_content);//联系人图片
            textView = (TextView)view.findViewById(R.id.text_item_content);//联系人名字
            line = (LinearLayout)view.findViewById(R.id.linear_line_itemcontent);//细微下滑线；
            gray = (LinearLayout)view.findViewById(R.id.view_gray_itemcontent);//灰色分割区域
        }
    }
    public ContentRecyclerAdapter(List<MyContent> c_list){
         mlistContent = c_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_recycler,parent,false);
        final MyViewHolder holder = new MyViewHolder(view);
        //点击事件的处理
        holder.contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int postion = holder.getAdapterPosition();//获得适配器的哪一个元素
                MyContent content = mlistContent.get(postion);//获得相应的实体
                Intent intent  = new Intent();
               //把联系人的名字，和图片传入对象,数据过大，导致传递失败
                Bitmap bitmap1 = content.getMbitmap();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                //****当然设置的图片需要保存至数据库端,这里先保存至本地
                SharedPreferences.Editor editor = parent.getContext().getSharedPreferences(
                        Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE).edit();
                //把图片转化为json,存储到sharePreference中
                String tojsoner = new String(android.util.Base64.encodeToString(baos.toByteArray(),
                        Base64.DEFAULT));
                editor.putString("contentpicture", tojsoner);
                editor.apply();


                intent.putExtra("contentname",content.getMfriendname());//传递姓名
                intent.putExtra("contentusename",content.getmUsename());//传递 联系人的usename
                intent.setClass(parent.getContext(), DataActivity.class);//跳转到资料卡中
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
         //列表数据绑定
        MyContent content = mlistContent.get(position);
        holder.imagView.setMbitmap(content.getMbitmap());
        holder.textView.setText(content.getMfriendname());
        if (content.isMline()){
            holder.line.setVisibility(View.VISIBLE);
            holder.gray.setVisibility(View.GONE);
        }else{
            holder.line.setVisibility(View.GONE);
            holder.gray.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mlistContent.size();
    }
}
