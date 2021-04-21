package com.johnson.qrcodedemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import com.johnson.qrcodedemo.http.NameValuePair;
import com.johnson.qrcodedemo.http.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParkDetailActivity extends AppCompatActivity {

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

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_detail);
        List<String> data = initData();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager = new GridLayoutManager(this, 3, OrientationHelper.VERTICAL, false);
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
//                Toast.makeText(ParkDetailActivity.this,"click " + position + " item", Toast.LENGTH_SHORT).show();
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
        initInfoDialog();

    }

    private void initInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 获取布局
        View view2 = View.inflate(this, R.layout.park_detail_layout, null);
        // 获取布局中的控件
        etChepai = view2.findViewById(R.id.et_chepai);
        etPhone = view2.findViewById(R.id.et_phone);
        btnConfirm = view2.findViewById(R.id.btn_confirm);
        // 设置参数
        builder.setTitle("车牌信息").setIcon(R.drawable.ic_launcher_background).setView(view2);
        // 创建对话框
        detailDialog = builder.show();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                detailDialog.dismiss();
                // 填写信息
                chepai = etChepai.getText().toString();
                if(chepai == null || "".equals(chepai)){
                    Toast.makeText(ParkDetailActivity.this, "请填入车牌", Toast.LENGTH_SHORT).show();
                    return;
                }
                phone = etPhone.getText().toString();
                if(phone == null || "".equals(phone)){
                    Toast.makeText(ParkDetailActivity.this, "请填入联系电话", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
        detailDialog.dismiss();
    }

    private List<String> initData() {
        List<String> data = new ArrayList<String>();
        for (int i = 1; i < 11; i++) {
            data.add(i+"");
        }
        return data;
    }

    public void onParkClick(View view) {

        if(selectedIndex == 0){
            Toast.makeText(this, "请选择车位", Toast.LENGTH_SHORT).show();
        }else {
            if ( chepai == null || chepai.length()<=0 || phone ==null || phone.length()<=0) {
                Toast.makeText(this, "请录入车牌信息", Toast.LENGTH_SHORT).show();
            } else {
                updateData(selectedIndex);
            }
        }
    }

    public void updateData(int selectedIndex){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        ArrayList<NameValuePair> param = new ArrayList<>();
                        param.add(new NameValuePair("index",selectedIndex+""));
                        param.add(new NameValuePair("phone",phone));
                        param.add(new NameValuePair("chepai",chepai));
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(OkHttpUtil.SET_DATA_URL,param));
                        new Handler(MainActivity.getActivity().getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                Toast.makeText(ParkDetailActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                bundle.putInt("index", selectedIndex);
                                bundle.putString("chepai", chepai);
                                bundle.putString("phone", phone);
                                Intent intent = new Intent(ParkDetailActivity.this, NavigateActivity.class);
                                intent.putExtra("data", bundle);
                                startActivity(intent);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(ParkDetailActivity.this, "预约失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ParkDetailActivity.this, "预约失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    public void onInfoClick(View view) {
        detailDialog.show();
    }

    @Override
    public void onBackPressed() {
//        return true
//        super.onBackPressed();
    }
}