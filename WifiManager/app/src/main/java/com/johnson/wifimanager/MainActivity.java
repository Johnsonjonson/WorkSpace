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
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
//import android.net.wifi.WifiNetworkSpecifier;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.Toast;


import com.hacknife.wifimanager.IWifi;
import com.hacknife.wifimanager.IWifiManager;
import com.hacknife.wifimanager.MyWifiManager;
import com.hacknife.wifimanager.OnWifiChangeListener;
import com.hacknife.wifimanager.OnWifiConnectListener;
import com.hacknife.wifimanager.OnWifiStateChangeListener;
import com.hacknife.wifimanager.State;
import com.hacknife.wifimanager.Wifi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnWifiChangeListener, OnWifiConnectListener, OnWifiStateChangeListener {
    private static final String TAG = "Johnson";

    private List<ScanResult> wifiList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NormalAdapter mAdapter;
    protected WifiAdmin mWifiAdmin;
    protected WifiUtils mWifiUtils;
    private IWifiManager iWifiManager;
    private Context mContext;
    private MyWifiManager myWifiManager;
    private String currentWifiConnectSSID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        connectEncryptWifi(IWifi wifi, String password)
        mContext = this;
        initWifiManager();
        mWifiAdmin = new WifiAdmin(MainActivity.this);
        mWifiUtils = new WifiUtils(MainActivity.this);
        initRecyclerView();
        registerPermission();
    }

    private void initWifiManager() {
        myWifiManager = new MyWifiManager(this);
        myWifiManager.setOnWifiChangeListener(this);
        myWifiManager.setOnWifiConnectListener(this);
        myWifiManager.setOnWifiStateChangeListener(this);
    }

//    public boolean isRegiester(){
//        Intent intent = new Intent();
//        IntentFilter filter = new IntentFilter(
//                WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        intent.setAction("com.xxx.powersaving.INSTALLAPP");
//        intent.putExtra("path", apkPath);
//        PackageManager pm = context.getPackageManager();
//        List<ResolveInfo> resolveInfos = pm.queryBroadcastReceivers(intent, 0);
//        if(resolveInfos != null && !resolveInfos.isEmpty()){
//            //查询到相应的BroadcastReceiver
//        }
//        return fale
//    }



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
//                MainActivity.this.unregisterReceiver(mReceiver);
//                selectedIndex = position+1;
//                updateSelect(position);
                ScanResult scanResult = wifiList.get(position);

//                Toast.makeText(MainActivity.this," click " + position + " item", Toast.LENGTH_SHORT).show();
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
                WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                connectionInfo.getNetworkId();
                String wifiSSID = connectionInfo
                        .getSSID();
                int ipAddress = connectionInfo.getIpAddress();
                @SuppressLint("MissingPermission") List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();


                if (!capabilities.equals("")) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    String ssid = scanResult.SSID;
                    alert.setTitle(ssid);
                    alert.setMessage("输入密码");
                    final EditText et_password = new EditText(MainActivity.this);
                    et_password.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                    final SharedPreferences preferences = getSharedPreferences("wifi_password", Context.MODE_PRIVATE);
                    et_password.setText(preferences.getString(ssid, ""));
                    alert.setView(et_password);
                    //alert.setView(view1);

                    alert.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            currentWifiConnectSSID = scanResult.SSID;
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
//                            mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo1(scanResult, et_password.getText().toString(), 3));
//                            mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo(ssid, et_password.getText().toString(), 3));
                            mWifiUtils.openWifi();
//                            mWifiUtils.addNetwork(mWifiUtils.createWifiInfo(ssid, et_password.getText().toString(), WifiUtils.WifiCipherType.WIFICIPHER_WPA),ssid);
                            mWifiUtils.addNetwork(mWifiUtils.createWifiInfo(ssid, et_password.getText().toString(), WifiUtils.WifiCipherType.WIFICIPHER_WPA),ssid);
//                            ScanResult result, List<WifiConfiguration > configurations, String connectedSSID, int ipAddress

//                            myWifiManager.connectEncryptWifi(Wifi.create(scanResult, configuredNetworks, wifiSSID, ipAddress),et_password.getText().toString());
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
                                            currentWifiConnectSSID = scanResult.SSID;
                                            IntentFilter filter = new IntentFilter(
                                                    WifiManager.NETWORK_STATE_CHANGED_ACTION);
                                            //="android.net.wifi.STATE_CHANGE"  监听wifi状态的变化
                                            registerReceiver(mReceiver, filter);

//                                            mWifiUtils.openWifi();
//                                            mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo(scanResult.SSID, "", 1));
//                                            mWifiUtils.addNetwork(mWifiUtils.createWifiInfo(scanResult.SSID, "", WifiUtils.WifiCipherType.WIFICIPHER_NOPASS));
//                                            myWifiManager.connectOpenWifi(Wifi.create(scanResult, configuredNetworks, wifiSSID, ipAddress));
                                            mWifiUtils.openWifi();
//                                            mWifiUtils.addNetwork(mWifiUtils.createWifiInfo(scanResult.SSID, "", WifiUtils.WifiCipherType.WIFICIPHER_NOPASS),scanResult.SSID);
                                            mWifiUtils.addNetwork(mWifiUtils.createWifiInfo(scanResult.SSID, "", WifiUtils.WifiCipherType.WIFICIPHER_NOPASS),scanResult.SSID);
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
        wifiList.clear();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        List<ScanResult> scanWifiList = wifiManager.getScanResults();
        if (scanWifiList == null) {
            if(wifiManager.getWifiState()==3){
                Toast.makeText(this,"当前区域没有无线网络",Toast.LENGTH_SHORT).show();
            }else if(wifiManager.getWifiState()==2){
                Toast.makeText(this,"wifi正在开启，请稍后扫描", Toast.LENGTH_SHORT).show();
            }else{Toast.makeText(this,"WiFi没有开启", Toast.LENGTH_SHORT).show();
            }
        }
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
//                if(wifiSSID.equals(currentWifiConnectSSID)){
//                    return;
//                }
//                Toast.makeText(context, wifiSSID+"连接成功", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this,PingActivity.class));
            }
        }

    };

    public void onRefreshClick(View view) {
        getWifiList();
    }

    @Override
    public void onWifiChanged(List<IWifi> wifis) {

    }

    @Override
    public void onConnectChanged(boolean status) {
        Log.d(TAG,"连接状态"+status);
    }

    @Override
    public void onStateChanged(State state) {

    }
}