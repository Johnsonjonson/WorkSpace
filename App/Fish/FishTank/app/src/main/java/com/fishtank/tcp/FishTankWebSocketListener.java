package com.fishtank.tcp;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fishtank.activity.MainActivity;
import com.fishtank.bean.FishTankBean;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class FishTankWebSocketListener extends WebSocketListener {
    Gson gson = new Gson();
    private disConnectListener listener;

    public FishTankWebSocketListener(disConnectListener listener) {
        this.listener = listener;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d("WebSocket","打开链接");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        SocketManager.resetHeartTime();
        FishTankBean authBean = gson.fromJson(text, FishTankBean.class);
        if (TextUtils.equals(authBean.getCmd(), "heartbeat")) { //发送认证消息
            return;
        }
        output("onMessage: " + text);

        EventBus.getDefault().post(new MessageServer(text));
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        output("onMessage byteString: " + bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        output("onClosing: " + code + "/" + reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        output("onClosed: " + code + "/" + reason);
    }

    @Override

    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        output("onFailure: " + t.getMessage());
        listener.reconnect();
    }

    private void output(String params) {
        System.out.println(params);
    }

    private String sendData(String sign) {
        String jsonHead = "";
        Map<String, Object> mapHead = new HashMap<>();
        mapHead.put("cmd", "auth2");
        mapHead.put("msg_id", "1");
        mapHead.put("authCode", sign);
        mapHead.put("userId", "111");
        jsonHead = buildRequestParams(mapHead);
        Log.e("TAG", "sendData: " + jsonHead);
        return jsonHead;
    }

    public static String buildRequestParams(Object params) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(params);
        return jsonStr;
    }

    //定义失败回调的接口
    public interface disConnectListener {
        void reconnect();
    }
}
