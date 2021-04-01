package com.ilikexy.bigwork.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikexy.bigwork.MineFormationActivity;
import com.ilikexy.bigwork.R;
import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.entity.MyFound;
import com.ilikexy.bigwork.entity.MyMine;
import com.ilikexy.bigwork.recyclerview.FoundRecyclerAdapter;
import com.ilikexy.bigwork.zidingyi.RoundedSquare;

import org.w3c.dom.Text;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MineFragment extends Fragment implements View.OnClickListener{
    //构造函数传入 recyclerview数据 和 个人信息
    private List<MyFound> myListFound;//因为我的recycle和 发现中的recycler一样，所以可以套用adapter
    private MyMine myMine;
    private LinearLayout linear_mine_fragement;
    private RoundedSquare roundedSquare;
    private TextView text_fakename,text_usename;
    //recycler的处理
    private RecyclerView recyclerView;
    public MineFragment(List<MyFound> c_list,MyMine c_mymine){
        myListFound = c_list;
        myMine = c_mymine;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mine_mainfun,container,false);
        linear_mine_fragement = (LinearLayout)view.findViewById(R.id.linear_mine_fragment);
        roundedSquare = (RoundedSquare)view.findViewById(R.id.imag_mine_fragment);
        text_fakename = (TextView)view.findViewById(R.id.text_fakename_minefra);
        text_usename = (TextView)view.findViewById(R.id.text_usename_minefra);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_mine_fragment);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        FoundRecyclerAdapter adapter = new FoundRecyclerAdapter(myListFound);
        recyclerView.setAdapter(adapter);
        linear_mine_fragement.setOnClickListener(this);
        roundedSquare.setImageBitmap(myMine.getmBitmap());
        text_fakename.setText(myMine.getmFakeName());
        text_usename.setText(myMine.getmUseName());
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_mine_fragment://跳转到用户信息修改界面
                BaseActivity.jump(getContext(), MineFormationActivity.class);
                break;
            default:
                break;
        }
    }
}
