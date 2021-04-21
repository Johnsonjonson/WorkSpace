package com.johnson.qrcodedemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ParkDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NormalAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private int selectedIndex = 0;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_detail);
        List<String> data = initData();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager = new GridLayoutManager(this, 4, OrientationHelper.VERTICAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        mAdapter = new NormalAdapter(data);
        mAdapter.setOnItemClickListener(new NormalAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ParkDetailActivity.this,"click " + position + " item", Toast.LENGTH_SHORT).show();
                selectedIndex = position+1;
                for (int i = 0; i < 10; i++) {
                    if (i != position) {
//                        mAdapter.get
                        View childView = layoutManager.findViewByPosition(i);	//2为recyclerView中item位置，
                        //mLayoutManager为recyclre的布局管理器
                        LinearLayout layout = (LinearLayout)childView;		//获取布局中任意控件对象
                        LinearLayout parkLayout = (LinearLayout) layout.findViewById(R.id.park_layout_1);
                        parkLayout.setBackground(layout.getContext().getDrawable(R.drawable.layout_bg_normal));
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(ParkDetailActivity.this,"long click " + position + " item", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(mAdapter);
        //设置分隔线
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private List<String> initData() {
        List<String> data = new ArrayList<String>();
        for (int i = 1; i < 11; i++) {
            data.add(i+"");
        }
        return data;
    }

    public void onParkClick(View view) {
        if (selectedIndex>0) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", selectedIndex);
            Intent intent = new Intent(this, NavigateActivity.class);
            intent.putExtra("data", bundle);
            startActivity(intent);
        }else{
            Toast.makeText(this, "请选择车位", Toast.LENGTH_SHORT).show();
        }
    }
}