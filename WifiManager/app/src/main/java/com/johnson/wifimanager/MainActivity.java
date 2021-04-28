package com.johnson.wifimanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Johnson";
    ArrayList<String> addresslist = new ArrayList<>();
    HashSet<String> ipList = new HashSet<>();
    private List<ScanResult> wifiList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NormalAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initData();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (String s : addresslist) {
//                    String ip = PingUtil.pingUrl(s);
//                    ipList.add(ip);
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (ipList.size() == addresslist.size()){
//                            Toast.makeText(MainActivity.this, "正常", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(MainActivity.this, "异常", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//            }
//        }).start();



//
//        try {
//            PingUtil.pingUrl("www.baidu.com");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        initRecyclerView();
        registerPermission();
    }

    @SuppressLint("WrongConstant")
    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
//        layoutManager = new GridLayoutManager(this, 1, OrientationHelper.VERTICAL, false);
//        layoutManager.set
        //        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        mAdapter = new NormalAdapter(wifiList,this);
        mAdapter.setOnItemClickListener(new NormalAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(View view, int position) {
//                selectedIndex = position+1;
//                updateSelect(position);
                Toast.makeText(MainActivity.this," click " + position + " item", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this,"long click " + position + " item", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(mAdapter);
        //设置分隔线
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        addresslist.add("www.qq.com");
        addresslist.add("www.baidu.com");
        addresslist.add("www.baidu.com");
    }

    private class NetPing extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String s = "";
            s = PingUtil.pingUrl("www.baidu.com");
            Log.i("ping", "" + s);
            return s;
        }
    }

    private void registerPermission(){
        //动态获取定位权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},
                    100);

        } else {
            getWifiList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            getWifiList();
        }
    }

    public List<ScanResult> getWifiList() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        List<ScanResult> scanWifiList = wifiManager.getScanResults();
        if (scanWifiList != null && scanWifiList.size() > 0) {
            HashMap<String, Integer> signalStrength = new HashMap<String, Integer>();
            for (int i = 0; i < scanWifiList.size(); i++) {
                ScanResult scanResult = scanWifiList.get(i);
                Log.e(TAG, "搜索的wifi-ssid:" + scanResult.SSID);
                if (!scanResult.SSID.isEmpty()) {
                    String key = scanResult.SSID + " " + scanResult.capabilities;
                    if (!signalStrength.containsKey(key)) {
                        signalStrength.put(key, i);
                        wifiList.add(scanResult);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }else {
            Log.e(TAG, "没有搜索到wifi");
        }
        return wifiList;
    }
}