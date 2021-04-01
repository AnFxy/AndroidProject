package com.ilikexy.bigwork.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.entity.MyMessage;
import com.ilikexy.bigwork.recyclerview.MessRecyclerAdapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MessageFragment extends Fragment implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private List<MyMessage> mlistofMessage;
    private ImageView mImageView;
    //数据来源，当然也可以建立构造函数，从Activity中传入
    public MessageFragment(List<MyMessage> c_list){
        mlistofMessage  = c_list;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
       View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_message_mainfun,container,false);
       mImageView = (ImageView)view.findViewById(R.id.imag_nofun_messfragment);
       mImageView.setOnClickListener(this);
       mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_message_fragement);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());//上下文获取
        mRecyclerView.setLayoutManager(manager);
        MessRecyclerAdapter messRecyclerAdapter = new MessRecyclerAdapter(mlistofMessage);
        mRecyclerView.setAdapter(messRecyclerAdapter);
       return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imag_nofun_messfragment:
                Toast.makeText(getContext(),"暂时还未开发此功能",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
