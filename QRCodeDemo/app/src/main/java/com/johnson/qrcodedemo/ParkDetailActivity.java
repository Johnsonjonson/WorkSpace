package com.johnson.qrcodedemo;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.johnson.qrcodedemo.bean.Park;
import com.johnson.qrcodedemo.http.NameValuePair;
import com.johnson.qrcodedemo.http.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private int kongxian = 0;
    private int yuyue = 0;
    private int zhanyong = 0;
    private ArrayList<Park> parkList = new ArrayList<Park>();
    public static Activity mActivity;
    private AlertDialog queryDialog;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_detail);
        mActivity = this;
        initView();
        initRecyclerView();
        initInfoDialog();
        requestAllPark();
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
        for (int i = 0; i < 10; i++) {
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
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
    }
    private Timer timer;
    {
        timer = new Timer(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requestAllPark();
            }
        }, 1000, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
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

    public void onParkClick(View view) {

        if(selectedPark == null && selectedIndex==0){
            Toast.makeText(this, "请选择车位", Toast.LENGTH_SHORT).show();
        }else {
            if ( chepai == null || chepai.length()<=0 || phone ==null || phone.length()<=0) {
                Toast.makeText(this, "请录入车牌信息", Toast.LENGTH_SHORT).show();
            } else {
                bookPark();
            }
        }
    }

    public void bookPark(){
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
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(OkHttpUtil.BOOK_PARK_URL,param));
                        new Handler(mActivity.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                JSONObject jsParam = null;
                                try {
                                    jsParam = new JSONObject(result);
                                    int errorno = jsParam.optInt("errorno", 0);
                                    if(errorno ==1){
                                        Toast.makeText(ParkDetailActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("index", selectedIndex);
                                        bundle.putString("chepai", chepai);
                                        bundle.putString("phone", phone);
                                        Intent intent = new Intent(ParkDetailActivity.this, NavigateActivity.class);
                                        intent.putExtra("data", bundle);
                                        startActivity(intent);
                                    }else if(errorno ==-1){
                                        Toast.makeText(ParkDetailActivity.this, "车牌已预约,预约失败，请重试", Toast.LENGTH_SHORT).show();
                                    }else if(errorno ==-2){
                                        Toast.makeText(ParkDetailActivity.this, "车位已预约 ,预约失败，请重试", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ParkDetailActivity.this, "预约失败，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ParkDetailActivity.this, "预约失败，请重试", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

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

    private void requestAllPark() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.QUERY_PARK_URL);
                        new Handler(mActivity.getMainLooper()).post(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void run() {
                                try {
                                    // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
//                                    Log.d("result ===  ", result);
                                    JSONArray jsonArray = new JSONArray(result);
                                    parkList.clear();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        // JSON数组里面的具体-JSON对象
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        int status = jsonObject.optInt("status", 0);
                                        int id = jsonObject.optInt("id", 0);
                                        String chepai = jsonObject.optString("chepai", "");
                                        String phone = jsonObject.optString("phone", "");
                                        boolean isSelect = false;
                                        if (id == selectedIndex){
                                            isSelect = true;
                                        }
                                        Park park = new Park(status, id, chepai, phone,isSelect);

                                        parkList.add(park);
                                        // 日志打印结果：
//                                        Log.d(TAG, "analyzeJSONArray1 解析的结果：status" + status + " id:" + id);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    updatePark();
                                }catch (Exception e){

                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
//                        Toast.makeText(ParkDetailActivity.this, "预约失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ParkDetailActivity.this, "预约失败，请重试", Toast.LENGTH_SHORT).show();
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 获取布局
        View view2 = View.inflate(this, R.layout.park_query_layout, null);
        // 获取布局中的控件
        EditText etChepaiQuery = view2.findViewById(R.id.et_chepai);
        Button btnQuery = view2.findViewById(R.id.btn_query);
        // 设置参数
        builder.setTitle("查找车牌").setIcon(R.drawable.ic_launcher_background).setView(view2);
        // 创建对话框
        queryDialog = builder.show();

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 填写信息
                String chepai = etChepaiQuery.getText().toString();
                if(chepai == null || "".equals(chepai)){
                    Toast.makeText(ParkDetailActivity.this, "请填入车牌", Toast.LENGTH_SHORT).show();
                    return;
                }
                queryChepai(chepai);
            }
        });
        queryDialog.show();
    }

    private void queryChepai(String chepai) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParam(OkHttpUtil.QUERY_PARK_BY_CHEPAI,"chepai",chepai));
                        new Handler(mActivity.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                JSONObject jsParam = null;
                                try {
                                    jsParam = new JSONObject(result);
                                    int errorno = jsParam.optInt("errorno", 0);
                                    int selectedIndex = jsParam.optInt("id", 0);
                                    String chepai = jsParam.optString("chepai", "");
                                    String phone = jsParam.optString("phone", "");
                                    if(errorno == 1){
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("index", selectedIndex);
                                        bundle.putString("chepai", chepai);
                                        bundle.putString("phone", phone);
                                        Intent intent = new Intent(ParkDetailActivity.this, NavigateActivity.class);
                                        intent.putExtra("data", bundle);
                                        queryDialog.dismiss();
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(ParkDetailActivity.this, "未找到相应车牌信息，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}