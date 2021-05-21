package com.johnson.bluetooth.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.johnson.bluetooth.ItemClickListener;
import com.johnson.bluetooth.MacBean;
import com.johnson.bluetooth.MyAdapter;
import com.johnson.bluetooth.R;
import com.johnson.bluetooth.utils.PermissionUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BluetoothClient mClient;
    private SearchRequest request;
    private Map<String,Beacon>  devices= new HashMap<String,Beacon>();
    private RecyclerView mRecycleView;
    private ArrayList<String> macs = new ArrayList<>();
    private ArrayList<MacBean> macBeans = new ArrayList<>();
    private HashMap<String,String> macDatas = new HashMap<String,String>();
    private RecyclerView.Adapter mAdapter;
    public static Activity mActivity;
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket clientSocket;
    private OutputStream os;
    private UUID MYUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    //写通道uuid
    private static final UUID writeCharactUuid = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    //通知通道 uuid
    private static final UUID notifyCharactUuid =UUID.fromString( "0000ffe0-0000-1000-8000-00805f9b34fb");
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristicNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
//        initData();
        initSwitch();
        initListView();
        initBlueTooth();
        searchDevices();
    }

    private void searchDevices(){
//        if(mBluetoothAdapter.is()){
//
//        }
        //10s后停止搜索
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }, 1000 * 10);

        String service_uuid = "0000ffe1-0000-1000-8000-00805f9b34fb";
        UUID[] serviceUuids = {UUID.fromString(service_uuid)};
//        mBluetoothAdapter.startLeScan(serviceUuids, mLeScanCallback);
        mBluetoothAdapter.startLeScan( mLeScanCallback);
    }

    private void initSwitch() {
        Switch mSwitchBT = (Switch)findViewById(R.id.switch_bluetooth);
        mSwitchBT.setChecked(mBluetoothAdapter.isEnabled());

        mSwitchBT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mBluetoothAdapter.enable();  //  打开蓝牙
                }else {
                    mBluetoothAdapter.disable(); //  关闭蓝牙
                }
            }
        });
    }



    private void initListView() {
        mRecycleView = findViewById(R.id.recycle_view);
        mRecycleView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));   //设置水平分割线
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));   //recycleView必须要布局管理器
        mAdapter = new MyAdapter(macBeans,new ItemClickListener(){
            @Override
            public void onItemClick(String mac) {
//                if(mBluetoothAdapter.isDiscovering()){
//                    mBluetoothAdapter.cancelDiscovery();
//                }
                //获取所需地址
                Toast.makeText(MainActivity.this, "正在连接 ："+mac, Toast.LENGTH_SHORT).show();
                BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(mac);
                String mDeviceAddress = remoteDevice.getAddress();
                mBluetoothGatt = remoteDevice.connectGatt(MainActivity.this, false, mGattCallback);
//                if (null == clientSocket) {
//                    try {
//                        clientSocket = remoteDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
//                        if(clientSocket.isConnected()){
//                            Toast.makeText(MainActivity.this,"已连接成功", Toast.LENGTH_SHORT).show();
//                        }else{
//                            //开始连接蓝牙设备
//                            clientSocket.connect();
//                            if(clientSocket.isConnected()){
//                                Toast.makeText(MainActivity.this,"已连接成功", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
////                        os = clientSocket.getOutputStream();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                    if (null != os) {
//                        //向服务器端发送一个字符串
//                        try {
//                            os.write("这是另一台手机发送过来的数据".getBytes("utf-8"));
//                            Toast.makeText(MainActivity.this,"发送成功", Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            Toast.makeText(MainActivity.this,"发送失败", Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    }
//                }else{
//                    if (clientSocket.isConnected()){
//                        Toast.makeText(MainActivity.this,"已连接一台设备", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });           //实例化适配器
        mRecycleView.setAdapter(mAdapter);     //设置适配器
    }


    public void onBtnSearch(View view){
        macBeans.clear();
        macs.clear();
        mAdapter.notifyDataSetChanged();
        searchDevices();
//        initBlueTooth();
    }

    public void onBtnSend(View view){
        EditText etView = findViewById(R.id.et_data);
        String sData = etView.getText().toString();
        if(mBluetoothGattCharacteristic != null) {
            mBluetoothGattCharacteristic.setValue("000");
            if (mBluetoothGatt != null) {
                mBluetoothGatt.setCharacteristicNotification(mBluetoothGattCharacteristicNotify , true);
                mBluetoothGatt.writeCharacteristic(mBluetoothGattCharacteristic );
            }
        }
    }

    private final BluetoothBondListener mBluetoothBondListener = new BluetoothBondListener() {
        @Override
        public void onBondStateChanged(String mac, int bondState) {
            // bondState = Constants.BOND_NONE, BOND_BONDING, BOND_BONDED
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void initBlueTooth() {
        if (PermissionUtil.checkCoarsePermissions() && PermissionUtil.checkFinePermissions()) {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    String str = device.getName() + "|" + device.getAddress();
                    System.out.println("BlueTooth已配对的设备:"+str);
                    macs.add(str);
//                macDatas.put(device.getAddress(),device.getName());
                    macBeans.add(new MacBean(device.getName(),device.getAddress()));
                    mAdapter.notifyItemInserted(macBeans.size() + 1);
                }
            }
            if (!mBluetoothAdapter.isDiscovering()) {
                //搜索蓝牙设备
                mBluetoothAdapter.startDiscovery();
            }
            // 注册Receiver来获取蓝牙设备相关的结果
            IntentFilter intent = new IntentFilter();
            intent.addAction(BluetoothDevice.ACTION_FOUND); // 用BroadcastReceiver来取得搜索结果
            intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            registerReceiver(searchDevices, intent);
        }
    }
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) { //found device
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String str = device.getName() + "|" + device.getAddress();
                System.out.println("BlueTooth搜索到的设备:"+str);
                macs.add(str);
                macBeans.add(new MacBean(device.getName(),device.getAddress()));
                mAdapter.notifyItemInserted(macBeans.size() + 1);
                List list = new ArrayList();
                //如果List中没有str元素则返回-1
                if (list.indexOf(str) == -1)// 防止重复添加
                    list.add(str); // 获取设备名称和mac地址

            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                Toast.makeText(getBaseContext(), "正在扫描", Toast.LENGTH_SHORT).show();
            } else if (action
                    .equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Toast.makeText(getBaseContext(), "扫描完成，点击列表中的设备来尝试连接", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
//
//        mClient.unregisterBluetoothBondListener(mBluetoothBondListener);
    }

    //蓝牙扫描回调接口
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback(){
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if (device.getName() == null) {
                return;
            }
            macBeans.add(new MacBean(device.getName(),device.getAddress()));
            mAdapter.notifyItemInserted(macBeans.size() + 1);
            Log.e("--->搜索到的蓝牙名字：", device.getName());
            //可以将扫描的设备弄成列表，点击设备连接，也可以根据每个设备不同标识，自动连接。

        }
    };

    // BLE回调操作
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState){
            super.onConnectionStateChange(gatt, status, newState);

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // 连接成功
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    }
                });
                mBluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "断开连接", Toast.LENGTH_SHORT).show();
                    }
                });
                // 连接断开
                Log.d("TAG","onConnectionStateChange fail-->" + status);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //发现设备，遍历服务，初始化特征
                initBLE(gatt);
            } else {
                Log.d("TAG","onServicesDiscovered fail-->" + status);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 收到的数据
                Log.d(TAG,"接收到数据");
                byte[] receiveByte = characteristic.getValue();

            }else{
                Log.d("TAG","onCharacteristicRead fail-->" + status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic){
            super.onCharacteristicChanged(gatt, characteristic);
            //当特征中value值发生改变
        }

        /**
         * 收到BLE终端写入数据回调
         * @param gatt
         * @param characteristic
         * @param status
         */
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 发送成功
                Log.d(TAG,"发送成功");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                // 发送失败
                Log.d(TAG,"发送失败");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {

            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {

            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {

            }
        }
    };

    //初始化特征
    public void initBLE(BluetoothGatt gatt) {
        if (gatt == null) {
            return;
        }
        //遍历所有服务
        for (android.bluetooth.BluetoothGattService BluetoothGattService : gatt.getServices()) {
            Log.e(TAG, "--->BluetoothGattService" + BluetoothGattService.getUuid().toString());

            //遍历所有特征
            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : BluetoothGattService.getCharacteristics()) {
                Log.e("---->gattCharacteristic", bluetoothGattCharacteristic.getUuid().toString());

                String str = bluetoothGattCharacteristic.getUuid().toString();
//                if (str.equals(writeCharactUuid)) {
//                    //根据写UUID找到写特征
                    mBluetoothGattCharacteristic = bluetoothGattCharacteristic;
//                } else if (str.equals(notifyCharactUuid)) {
//                    //根据通知UUID找到通知特征
                    mBluetoothGattCharacteristicNotify = bluetoothGattCharacteristic;
//                }
            }
        }
    }
}
