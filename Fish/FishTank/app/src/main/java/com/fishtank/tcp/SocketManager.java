package com.fishtank.tcp;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.fishtank.activity.MainActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class SocketManager {
    private static WebSocket mSocket;
    private static Request request;
    private static FishTankWebSocketListener listener;
    public static final String socketUrl = "ws://129.204.232.210:8080/fish/fishsocket";

    private static OkHttpClient client;
    private static long sendTime = 0L;
    // 发送心跳包
    private static Handler mHandler = new Handler();
    // 每隔2秒发送一次心跳包，检测连接没有断开
    private static final long HEART_BEAT_RATE = 5 * 1000;

    private static int HEART_BEAT_MAX = 3;
    private static int heartBeatTime = 0;

    // 发送心跳包
    private static Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if(heartBeatTime > HEART_BEAT_MAX){
                reconnect();
                return;
            }
            heartBeatTime = heartBeatTime +1;
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                String message = sendData();
                mSocket.send(message);
                sendTime = System.currentTimeMillis();
            }
            if (mHandler != null) {
                mHandler.postDelayed(this, HEART_BEAT_RATE); //每隔一定的时间，对长连接进行一次心跳检测
            }
        }
    };
    public static void connectSocket(){
        initSocket();
        request = new Request.Builder().url(socketUrl).build();
        client = new OkHttpClient();
        listener = new FishTankWebSocketListener(new FishTankWebSocketListener.disConnectListener() {
            @Override
            public void reconnect() {
                if(mHandler!=null){
                    mSocket = client.newWebSocket(request, listener);
                }
            }
        });
        mSocket = client.newWebSocket(request, listener);
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
    }

    private static void initSocket() {
        disconnect();
//        request = null;
//        client = null;
//        listener = null;
    }

    private static String sendData() {
        String jsonHead = "";
        Map<String, Object> mapHead = new HashMap<>();
        mapHead.put("cmd", "heartbeat");

        jsonHead = buildRequestParams(mapHead);
        Log.e("TAG", "sendData: " + jsonHead);
        return jsonHead;
    }

    public static String buildRequestParams(Object params) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(params);
        return jsonStr;
    }

    public static void clearSocket(){
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        if(mSocket!=null) {
            mSocket.cancel();
            mSocket.close(1000, null);
        }
        heartBeatTime = 0;
    }

    public static void disconnect() {
        heartBeatTime = 0;
        if (mSocket != null){
            mSocket.cancel();
            mSocket.close(1000, null);
        }
    }

    public static void reconnect(){
        Toast.makeText(MainActivity.getActivity(), "服务器连接断开，正在重连。。。", Toast.LENGTH_LONG).show();
        heartBeatTime = 0;
        disconnect();
        connectSocket();
    }

    public static void resetHeartTime(){
        heartBeatTime = 0;
    }
}
