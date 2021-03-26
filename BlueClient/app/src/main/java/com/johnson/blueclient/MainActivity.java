package com.johnson.blueclient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.net.wifi.aware.DiscoverySession;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    UUID MY_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private InputStream is;
    private OutputStream os;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         handler = new Handler(){
             @Override
             public void handleMessage(@NonNull Message msg) {
                 super.handleMessage(msg);
                 if (msg.what == 0x11) {
                     Bundle bundle = msg.getData();
                     String date = bundle.getString("msg");
                     Toast.makeText(MainActivity.this, date, Toast.LENGTH_SHORT).show();
                 }
             }
         };
        try {
            new AcceptThread(handler).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        handler.handleMessage();
    }

    private class AcceptThread extends Thread {
        public Handler handler;
        private final BluetoothServerSocket mServerSocket;
        public AcceptThread(Handler handler) throws IOException {
            mServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("NAME", MY_UUID);
            this.handler = handler;
        }

        @Override
        public void run(){
            BluetoothSocket socket = null;
            while(true){
                // TODO Auto-generated method stub
                //等待接受蓝牙客户端的请求
                try {
                    socket = mServerSocket.accept();
                    is = socket.getInputStream();
                    os = socket.getOutputStream();
                    while(true){
                        byte[] buffer = new byte[128];
                        int count = is.read(buffer);
//                        message.obj = new String(buffer,0, count, "utf-8");
                        final String msg= new String(buffer,0, count, "utf-8");
                        Bundle bundle = new Bundle();
                        Message message = Message.obtain();
                        message.setData(bundle);   //message.obj=bundle  传值也行
                        message.what = 0x11;
                        bundle.putString("msg",msg);
                        Log.d("BlueClient",msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        public void cancle(){
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

