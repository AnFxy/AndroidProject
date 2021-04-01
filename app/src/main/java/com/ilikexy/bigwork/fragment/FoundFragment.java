package com.ilikexy.bigwork.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.entity.MyFound;
import com.ilikexy.bigwork.recyclerview.FoundRecyclerAdapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FoundFragment extends Fragment implements View.OnClickListener{
    private List<MyFound> myFoundList;
    private RecyclerView myrecycler;
    private FoundRecyclerAdapter foundRecyclerAdapter;
    private ImageView mImageView;
    public FoundFragment(List<MyFound> c_list){
        myFoundList = c_list;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_found_mainfun,container,false);
        myrecycler = (RecyclerView)view.findViewById(R.id.recycler_found_fragment);
        mImageView  = (ImageView)view.findViewById(R.id.imag_nofun_foundfragment);
        mImageView.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        myrecycler.setLayoutManager(manager);
        foundRecyclerAdapter = new FoundRecyclerAdapter(myFoundList);
        myrecycler.setAdapter(foundRecyclerAdapter);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imag_nofun_foundfragment:
                Toast.makeText(getContext(),"暂未开发此功能",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
