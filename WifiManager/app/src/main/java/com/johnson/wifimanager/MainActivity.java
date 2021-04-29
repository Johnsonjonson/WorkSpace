package com.johnson.wifimanager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Johnson";

    private List<ScanResult> wifiList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NormalAdapter mAdapter;
    protected WifiAdmin mWifiAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWifiAdmin = new WifiAdmin(MainActivity.this);
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
                ScanResult scanResult = wifiList.get(position);

                Toast.makeText(MainActivity.this," click " + position + " item", Toast.LENGTH_SHORT).show();
                String capabilities = "";

                if (scanResult.capabilities.contains("WPA2-PSK")) {
                    // WPA-PSK加密
                    capabilities = "psk2";
                } else if (scanResult.capabilities.contains("WPA-PSK")) {
                    // WPA-PSK加密
                    capabilities = "psk";
                } else if (scanResult.capabilities.contains("WPA-EAP")) {
                    // WPA-EAP加密
                    capabilities = "eap";
                } else if (scanResult.capabilities.contains("WEP")) {
                    // WEP加密
                    capabilities = "wep";
                } else {
                    // 无密码
                    capabilities = "";
                }

                if (!capabilities.equals("")) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    String ssid = scanResult.SSID;
                    alert.setTitle(ssid);
                    alert.setMessage("输入密码");
                    final EditText et_password = new EditText(MainActivity.this);
                    final SharedPreferences preferences = getSharedPreferences("wifi_password", Context.MODE_PRIVATE);
                    et_password.setText(preferences.getString(ssid, ""));
                    alert.setView(et_password);
                    //alert.setView(view1);
                    alert.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentFilter filter = new IntentFilter(
                                    WifiManager.NETWORK_STATE_CHANGED_ACTION);
                            //="android.net.wifi.STATE_CHANGE"  监听wifi状态的变化
                            registerReceiver(mReceiver, filter);
                            String pw = et_password.getText().toString();
                            if (null == pw || pw.length() < 8) {
                                Toast.makeText(MainActivity.this, "密码至少8位", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(ssid, pw);   //保存密码
                            editor.commit();
                            mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo(ssid, et_password.getText().toString(), 3));
                        }
                    });
                    alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                            //mWifiAdmin.removeWifi(mWifiAdmin.getNetworkId());
                        }
                    });
                    alert.create();
                    alert.show();
                }else{
                    // 无密码
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage("你选择的wifi无密码，可能不安全，确定继续连接？")
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {
                                            IntentFilter filter = new IntentFilter(
                                                    WifiManager.NETWORK_STATE_CHANGED_ACTION);
                                            //="android.net.wifi.STATE_CHANGE"  监听wifi状态的变化
                                            registerReceiver(mReceiver, filter);
                                            mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo(scanResult.SSID, "", 1));
                                        }
                                    })
                            .setNegativeButton("取消",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {
                                            return;
                                        }
                                    }).show();
                }
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

    //监听wifi状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver (){
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(wifiInfo.isConnected()){
                WifiManager wifiManager = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                connectionInfo.getNetworkId();
                String wifiSSID = connectionInfo
                        .getSSID();
                Toast.makeText(context, wifiSSID+"连接成功", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this,PingActivity.class));
            }
        }

    };
}