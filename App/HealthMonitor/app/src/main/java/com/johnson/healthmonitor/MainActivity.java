package com.johnson.healthmonitor;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.johnson.healthmonitor.bean.HealthBean;
import com.johnson.healthmonitor.http.NameValuePair;
import com.johnson.healthmonitor.http.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static Context mActivity;
    private boolean isAlarm = false;
    private Timer timer;
    private TextView heartRate;
    private TextView bloodOxygen;
    private int anmo = 0;
    private int dianliao = 0;
    private Switch switchDianliao;
    private Switch switchAnmo;

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
        bloodOxygen = findViewById(R.id.bloodOxygen);
        bloodOxygen.setText("血氧：");
        bloodOxygen.setTextColor(getResources().getColor(R.color.green));
        heartRate = findViewById(R.id.heartRate);
        heartRate.setText("心率：" );
        heartRate.setTextColor(getResources().getColor(R.color.green));

        switchDianliao = (Switch) findViewById(R.id.switchDianliao);
        switchAnmo = (Switch) findViewById(R.id.switchAnmo);

        switchDianliao.setChecked(false);
        switchAnmo.setChecked(false);

//        switchDianliao.setSwitchTextAppearance(MainActivity.this,R.style.x1);

        switchDianliao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //控制开关字体颜色

                if (b) {

//                    switchDianliao.setSwitchTextAppearance(MainActivity.this,R.style.s_true);
                    dianliao = 1;
                }else {
                    dianliao = 0;
//                    switchDianliao.setSwitchTextAppearance(MainActivity.this,R.style.x1);

                }
                setControl();

            }

        });
        switchAnmo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //控制开关字体颜色

                if (b) {

//                    switchDianliao.setSwitchTextAppearance(MainActivity.this,R.style.s_true);
                    anmo = 1;
                }else {
                    anmo = 0;
//                    switchDianliao.setSwitchTextAppearance(MainActivity.this,R.style.x1);

                }
                setControl();

            }

        });
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
        requestCrtl();
    }

    public void requestCrtl(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    final String tempResult;
                    try {
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.GET_CONTROL);
                        new Handler(MainActivity.getActivity().getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                String[] split = result.split(",");
                                anmo = Integer.parseInt(split[0]);
                                dianliao = Integer.parseInt(split[1]);
                                switchDianliao.setChecked(anmo==1);
                                switchAnmo.setChecked(dianliao==1);
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

    public void requestData(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    final String tempResult;
                    try {
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.GET_DATA_URL);
                        new Handler(MainActivity.getActivity().getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                Gson gson = new Gson();
                                HealthBean healthBean = gson.fromJson(result, HealthBean.class);
                                JsonObject jsonObject = new JsonObject();
                                Log.d("result ===  ",healthBean.getHeartRate()+"    "+healthBean.getBloodOxygen());
//                                double parseDouble = Double.parseDouble(tempResult);
                                heartRate.setText("心率："+healthBean.getHeartRate() );
                                bloodOxygen.setText("血氧：" + healthBean.getBloodOxygen()   + "%");

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

    private void setControl(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        Log.d("Johnson","anmo = " + String.valueOf(anmo));
                        Log.d("Johnson","dianliao = " + String.valueOf(dianliao));
                        ArrayList<NameValuePair> list = new ArrayList();
                        NameValuePair nameValuePair = new NameValuePair("anmo",String.valueOf(anmo));
                        NameValuePair nameValuePair2 = new NameValuePair("dianliao",String.valueOf(dianliao));
                        list.add(nameValuePair);
                        list.add(nameValuePair2);

                        String url =  OkHttpUtil.attachHttpGetParams(OkHttpUtil.SET_CONTROL,list);
                        Log.d("Johnson ",url);
                        result = OkHttpUtil.getStringFromServer(url);
                        new Handler(MainActivity.getActivity().getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
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

    /**
     * 按摩
     * @param view
     */

    public void onBtnAnMoClick(View view) {
        anmo = 1;
        dianliao = 0;
        setControl();
    }

    /**
     * 电疗
     * @param view
     */
    public void onBtnDianLiaoClick(View view) {
        anmo = 0;
        dianliao = 1;
        setControl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        anmo = 0;
        dianliao = 0;
        setControl();
    }

    public void onBtnResetClick(View view) {
        anmo = 0;
        dianliao = 0;
        setControl();
    }
}