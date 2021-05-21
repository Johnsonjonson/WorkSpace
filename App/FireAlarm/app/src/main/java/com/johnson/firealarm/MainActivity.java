package com.johnson.firealarm;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.johnson.firealarm.bean.FireBean;
import com.johnson.firealarm.http.OkHttpUtil;
import com.johnson.firealarm.media.MediaManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static Context mActivity;
    private boolean isAlarm = false;
    private Timer timer;
    private TextView tipsView;
    private TextView tempView;

    {
        timer = new Timer(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //下面图1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //全屏显示
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

            //下面图2
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            //下面图3
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            getWindow().setAttributes(lp);
        }
        mActivity = this;
        tipsView = findViewById(R.id.textView);
        tipsView.setText("正常");
        tipsView.setTextColor(getResources().getColor(R.color.green));
        tempView = findViewById(R.id.tempView);
        tempView.setText("设备温度："+"0°C");
        tempView.setTextColor(getResources().getColor(R.color.green));
//        MediaManager.playEffert("fire_alarm.mp3");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requestData();
            }
        }, 1000, 1000);
    }

    public void requestData(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    final String tempResult;
                    try {
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.GET_DATA_URL);
//                        tempResult = OkHttpUtil.getStringFromServer(OkHttpUtil.GET_TEMP_DATA_URL);
                        new Handler(MainActivity.getActivity().getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                Gson gson = new Gson();
                                FireBean fireBean = gson.fromJson(result, FireBean.class);
//                                Log.d("result ===  ",tempResult);
//                                double parseDouble = Double.parseDouble(tempResult);
                                int status = fireBean.getStatus();
                                double temp = fireBean.getTemp();
                                tempView.setText("设备温度："+temp +  "°C");
                                if(status == 0){
                                    isAlarm = false;
                                    tipsView.setText("正常");
                                    tipsView.setTextColor(getResources().getColor(R.color.green));
                                    tempView.setTextColor(getResources().getColor(R.color.green));
                                    MediaManager.stop();
                                }else if(status == 1){
                                    tipsView.setText("火灾警报");
                                    tipsView.setTextColor(getResources().getColor(R.color.red));
                                    tempView.setTextColor(getResources().getColor(R.color.red));
                                    if(!MediaManager.isPlaying()) {
                                        MediaManager.playEffert("fire_alarm.mp3");
                                    }
                                    isAlarm = true;
                                }
//                                resultFishTankBean fishTankBean = gson.fromJson(result, FishTankBean.class);
//                                isChange = fishTankBean.isStatus();
//                                curTemp =fishTankBean.getTemp();
//                                updateView(false);
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

    public static Context getActivity() {
        return mActivity;
    }
}