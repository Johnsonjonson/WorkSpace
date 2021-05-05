package com.johnson.tencentmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

import com.johnson.tencentmap.bean.Park;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParkDetailActivity extends AppCompatActivity {

    private static final String TAG = "Johnson";
    private RecyclerView recyclerView;
    private NormalAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private int selectedIndex = 0;
    private Button btnConfirm;
    private AlertDialog detailDialog;
    private EditText etChepai;
    private EditText etPhone;
    private String chepai;
    private String phone;
    private Park selectedPark;
//    private TextView tvYuyue;
    private TextView tvKongxian;
    private TextView tvZhanyong;
    private TextView tvPark;
    private ImageButton btnPack;
    private FrameLayout framePack;
//    private
    private int kongxian = 0;
    private int yuyue = 0;
    private int zhanyong = 0;
    private ArrayList<Park> parkList = new ArrayList<Park>();
    public static Activity mActivity;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_detail);
        mActivity = this;
        int zhanyongIndex = 0;
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("data");
        if (bundle!=null) {
            zhanyongIndex = bundle.getInt("index");
        }
        initView();
        initRecyclerView();
        requestAllPark(zhanyongIndex);
    }

    private void initView() {
//        tvYuyue = findViewById(R.id.tv_yuyue);
        tvKongxian = findViewById(R.id.tv_kongxian);
        tvZhanyong = findViewById(R.id.tv_zhanyong);
        btnPack = findViewById(R.id.btn_park);
        framePack = findViewById(R.id.frame_park);
        tvPark = findViewById(R.id.tv_park);
    }

    @SuppressLint("WrongConstant")
    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager = new GridLayoutManager(this, 3, OrientationHelper.VERTICAL, false);
//        layoutManager.set
        //        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        mAdapter = new NormalAdapter(parkList,this);
        mAdapter.setOnItemClickListener(new NormalAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(View view, int position) {
                selectedIndex = position+1;
                updateSelect(position);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateSelect(int position) {
        for (int i = 0; i < 2; i++) {
            View childView = layoutManager.findViewByPosition(i);	//2为recyclerView中item位置，
            LinearLayout layout = (LinearLayout)childView;		//获取布局中任意控件对象
            LinearLayout parkLayout = (LinearLayout) layout.findViewById(R.id.park_layout_1);
            if (i != position) {
//                        mAdapter.get
                //mLayoutManager为recyclre的布局管理器
                parkLayout.setBackground(layout.getContext().getDrawable(R.drawable.layout_bg_normal));

            }else{
                parkLayout.setBackground(layout.getContext().getDrawable(R.drawable.layout_bg));
            }
        }
        updateYuyueBtn(position);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateYuyueBtn(int position) {
        if(position >=0 && parkList.get(position)!=null){
            Park park = parkList.get(position);
            selectedPark = park;
            int status = park.getStatus();
            if (status == 0) {
                btnPack.setEnabled(true);
                framePack.setEnabled(true);
                btnPack.setBackground(getApplication().getDrawable(R.drawable.kongxian_bg));
                framePack.setBackground(getApplication().getDrawable(R.drawable.kongxian_bg));
            } else {
                framePack.setEnabled(false);
                btnPack.setEnabled(false);
                btnPack.setBackground(getApplication().getDrawable(R.drawable.zhanyong_bg));
                framePack.setBackground(getApplication().getDrawable(R.drawable.zhanyong_bg));
            }

        }
    }

    public void onParkClick(View view) {

        if(selectedPark == null && selectedIndex==0){
            Toast.makeText(this, "请选择车位", Toast.LENGTH_SHORT).show();
        }else {
            bookPark();
        }
    }

    public void bookPark(){
        Toast.makeText(ParkDetailActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putInt("index", selectedIndex);
        Intent intent = new Intent(ParkDetailActivity.this, MainActivity.class);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }

    private void requestAllPark(int zhanyongIndex) {
        parkList.clear();
        ArrayList<Park> tempList = ParkManager.getParks(selectedIndex,zhanyongIndex);
        for (Park park : tempList) {
            parkList.add(park);
        }
        mAdapter.notifyDataSetChanged();
        updatePark();
    }

    private void updatePark() {
        kongxian = 0;
        yuyue = 0;
        zhanyong = 0;
        if (parkList.size() > 0) {
            for (Object o : parkList) {
                Park park = (Park) o;
                int status = park.getStatus();
                if (status == 0) { // 空闲
                    kongxian++;
                } else{// 占用中
                    zhanyong++;
                }
            }
            tvKongxian.setText("空闲：" + kongxian);
//            tvYuyue.setText("预约中：" + yuyue);
            tvZhanyong.setText("已占用：" + zhanyong);
        }

    }

    public void onInfoClick(View view) {
        detailDialog.show();
    }

    @Override
    public void onBackPressed() {
//        return true
//        super.onBackPressed();ßß
    }

    public void onQueryClick(View view) {
    }
}