package com.fishtank.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.fishtank.bean.FishTankBean;
import com.fishtank.http.NameValuePair;
import com.fishtank.http.OkHttpUtil;
import com.fishtank.media.MediaManager;
import com.fishtank.tcp.MessageServer;
import com.fishtank.tcp.SocketManager;
import com.google.gson.Gson;
import com.johnson.fishtank.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static Activity mContext = null;
    private TextView tempValue;
    private Button btnGet;
    Gson gson = new Gson();
    private TextView tvmsg;
    private boolean isChange;
    private boolean isNeedEffect;
    private double curTemp;
    private Button btnEffect;
    private Timer timer;

    {
        timer = new Timer(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGet = findViewById(R.id.btnGet);
        tempValue = findViewById(R.id.tempValue);
        btnEffect = findViewById(R.id.btnEffect);
        tvmsg = findViewById(R.id.tvmsg);
        mContext = this;
//        btnGet.setOnClickListener();
        //注册EventBus
        EventBus.getDefault().register(this);
        SocketManager.connectSocket();
        updateeffectSwitch();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
//        timer = new Timer(true);
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                requestData();
//            }
//        }, 1000, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
    }

    public void onBtnGetClick(View view){
        requestChange(!isChange);
    }

    public void onBtnFeeectClick(View view){
        isNeedEffect = !isNeedEffect;
        updateeffectSwitch();
    }

    //收到服务器的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageServer(MessageServer messageEvent) {
//        tempValue.setText("服务器收到:" +messageEvent.getMsg());
        Log.d("ccc",messageEvent.getResult());
        FishTankBean fishTankBean = gson.fromJson(messageEvent.getResult(), FishTankBean.class);
        double temp = fishTankBean.getTemp();
        boolean needChanged = fishTankBean.isStatus();
        curTemp =temp;
        isChange = needChanged;
        updateView(false);
    }

    public static Activity getActivity(){
        return mContext;
    }

    public void requestData(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.GET_DATA_URL);
                        new Handler(MainActivity.getActivity().getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                FishTankBean fishTankBean = gson.fromJson(result, FishTankBean.class);
                                isChange = fishTankBean.isStatus();
                                curTemp =fishTankBean.getTemp();
                                updateView(false);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestChange(boolean needChange){
        final boolean  tempChangeValue = needChange;
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                        nameValuePairs.add(new NameValuePair("change", String.valueOf(tempChangeValue)));
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(OkHttpUtil.BASE_URL, nameValuePairs));
                        new Handler(MainActivity.getActivity().getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                FishTankBean fishTankBean = gson.fromJson(result, FishTankBean.class);
                                isChange = fishTankBean.isStatus();
                                curTemp =fishTankBean.getTemp();
                                updateView(true);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updateView(boolean needEffect){
        tempValue.setText(curTemp+"°C");
        String fileName = "";
        if(isChange){
            btnGet.setText("关闭");
            btnGet.setGravity(Gravity.LEFT|Gravity.CENTER);
            btnGet.setBackground(getDrawable(R.drawable.open));
            fileName = "water.mp3";
            tvmsg.setText("正在换水");
        }else{
            btnGet.setText("打开");
            btnGet.setGravity(Gravity.RIGHT|Gravity.CENTER);
            btnGet.setBackground(getDrawable(R.drawable.close));
            MediaManager.stop();
            tvmsg.setText("已关闭换水");
        }
        if(needEffect && isNeedEffect && !fileName.isEmpty()) {
            MediaManager.playEffert(fileName);
        }
    }

    public void updateeffectSwitch(){
        if(isNeedEffect){
            btnEffect.setText("关闭");
            btnEffect.setGravity(Gravity.LEFT|Gravity.CENTER);
            btnEffect.setBackground(getDrawable(R.drawable.open));
        }else{
            btnEffect.setText("打开");
            btnEffect.setGravity(Gravity.RIGHT|Gravity.CENTER);
            btnEffect.setBackground(getDrawable(R.drawable.close));
            MediaManager.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.clearSocket();
    }
}
