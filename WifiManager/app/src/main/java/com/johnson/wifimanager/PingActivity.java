package com.johnson.wifimanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class PingActivity extends AppCompatActivity {

    private WifiAdmin wifiAdmin;
    ArrayList<String> addresslist = new ArrayList<>();
    HashSet<String> ipList = new HashSet<>();
    private ConnectivityManager.NetworkCallback networkCallback;
    private ConnectivityManager connectivityManager;
    private TextView pingResultView;
    private AlertDialog pingDialog;
    private AlertDialog.Builder resultDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);
        pingResultView = findViewById(R.id.ping_result);
        initData();
        wifiAdmin = new WifiAdmin(this);
        IntentFilter filter = new IntentFilter(
                WifiManager.NETWORK_STATE_CHANGED_ACTION);
        //="android.net.wifi.STATE_CHANGE"  监听wifi状态的变化
        registerReceiver(mReceiver, filter);

        initDialog();
    }

    private void initDialog() {
        pingDialog = new AlertDialog.Builder(PingActivity.this)
                 .setTitle("提示").setCancelable(false)
                 .setMessage("正在努力的Ping地址中，请稍候").show();
        pingDialog.dismiss();
        resultDialogBuilder = new AlertDialog.Builder(PingActivity.this);
    }

    public void onDisconnectWifi(View view) {
//        WifiAdmin wifiAdmin = new WifiAdmin(this);
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.disconnect();
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        int networkId = connectionInfo.getNetworkId();
        wifiAdmin.removeWifi(networkId);

//        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        wifi.disconnect();
//        discon = new DisconnectWifi();
//        registerReceiver(discon,new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
        if(networkCallback != null)
        {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }
    private void initData() {
        addresslist.add("www.qq.com");
        addresslist.add("www.baidu.com");
        addresslist.add("www.sogou.com");
    }

    public void onBtnPing(View view) {
        pingDialog.show();
        // 无密码
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String s : addresslist) {
                    String ip = PingUtil.pingUrl(s);
//                    String ip = PingUtil.getIps(result);
                    ipList.add(ip);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pingDialog.dismiss();
                        if (ipList.size() == addresslist.size()){
                            resultDialogBuilder.setTitle("提示").setMessage("无相同IP").show();
                        }else{
                            resultDialogBuilder.setTitle("警告").setMessage("有相同IP").show();
                        }
                    }
                });

            }
        }).start();
    }

    //监听wifi状态
    private BroadcastReceiver mReceiver = new BroadcastReceiver (){
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(!wifiInfo.isConnected()){
                Toast.makeText(context, "断开连接", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PingActivity.this,MainActivity.class));
            }
        }

    };
}