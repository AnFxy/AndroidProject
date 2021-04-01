package com.ilikexy.bigwork.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ilikexy.bigwork.AddFriendActivity;
import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.entity.MyContent;
import com.ilikexy.bigwork.recyclerview.ContentRecyclerAdapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContentFragment extends Fragment {
    private List<MyContent> myContentList;
    private RecyclerView recyclerView;
    public ContentFragment(List<MyContent> c_list){
        myContentList  = c_list;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_content_mainfun,container,false);
        //添加好友控件绑定
        ImageView imagAddFriend = (ImageView)view.findViewById(R.id.imag_addfriend_contentfra);
        imagAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到添加好友界面
                BaseActivity.jump(getContext(), AddFriendActivity.class);
            }
        });
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_content_fragement);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        //设置适配器
        ContentRecyclerAdapter adapter = new ContentRecyclerAdapter(myContentList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
