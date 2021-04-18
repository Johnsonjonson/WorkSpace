package com.johnson.gps;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.johnson.gps.bean.GPSBean;
import com.johnson.gps.http.NameValuePair;
import com.johnson.gps.http.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static Context mActivity;
    private boolean isAlarm = false;
    private Timer timer;
    private TextView weiDu;
    private TextView jingDu;
    private int deng = 0;
    private int fengming = 0;
    private Switch switchFengming;
    private Switch switchDeng;

    {
        timer = new Timer(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_title);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //下面图1
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            //全屏显示
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//
//            WindowManager.LayoutParams lp = getWindow().getAttributes();
//            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//
//            //下面图2
////        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//            //下面图3
////        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
//            getWindow().setAttributes(lp);
//        }
        mActivity = this;
        jingDu = findViewById(R.id.jingDu);
        jingDu.setText("");
        weiDu = findViewById(R.id.weiDu);
        weiDu.setText("" );

        switchFengming = (Switch) findViewById(R.id.switchFengming);
        switchDeng = (Switch) findViewById(R.id.switchDeng);

//        switchFengming.setChecked(false);
//        switchDeng.setChecked(false);

//        switchDianliao.setSwitchTextAppearance(MainActivity.this,R.style.x1);

        switchFengming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //控制开关字体颜色

                if (b) {

//                    switchDianliao.setSwitchTextAppearance(MainActivity.this,R.style.s_true);
                    fengming = 1;
                }else {
                    fengming = 0;
//                    switchDianliao.setSwitchTextAppearance(MainActivity.this,R.style.x1);

                }
                setControl();

            }

        });
        switchDeng.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //控制开关字体颜色

                if (b) {

//                    switchDianliao.setSwitchTextAppearance(MainActivity.this,R.style.s_true);
                    deng = 1;
                }else {
                    deng = 0;
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
                                deng = Integer.parseInt(split[0]);
                                fengming = Integer.parseInt(split[1]);
                                switchFengming.setChecked(deng ==1);
                                switchDeng.setChecked(fengming ==1);
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
                                GPSBean GPSBean = gson.fromJson(result, GPSBean.class);
                                JsonObject jsonObject = new JsonObject();
                                Log.d("result ===  ", GPSBean.getJingdu()+"    "+ GPSBean.getWeidu());
//                                double parseDouble = Double.parseDouble(tempResult);
                                weiDu.setText(""+ GPSBean.getJingdu() );
                                jingDu.setText("" + GPSBean.getWeidu());

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
                        Log.d("Johnson","deng = " + String.valueOf(deng));
                        Log.d("Johnson","fengming = " + String.valueOf(fengming));
                        ArrayList<NameValuePair> list = new ArrayList();
                        NameValuePair nameValuePair = new NameValuePair("fengming",String.valueOf(fengming));
                        NameValuePair nameValuePair2 = new NameValuePair("deng",String.valueOf(deng));
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
        deng = 1;
        fengming = 0;
        setControl();
    }

    /**
     * 电疗
     * @param view
     */
    public void onBtnDianLiaoClick(View view) {
        deng = 0;
        fengming = 1;
        setControl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deng = 0;
        fengming = 0;
        setControl();
    }

    public void onBtnResetClick(View view) {
        deng = 0;
        fengming = 0;
        setControl();
    }
}