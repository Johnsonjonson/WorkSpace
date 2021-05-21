package com.johnson.humidity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.johnson.humidity.bean.DataBean;
import com.johnson.humidity.http.NameValuePair;
import com.johnson.humidity.http.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static Context mActivity;
    private boolean isAlarm = false;
    private Timer timer;
    private TextView wendu;
    private TextView shidu;
    private int deng = 0;
    private int fengming = 0;
    private Switch switchFengming;
    private Switch switchDeng;
    private boolean isGetData;
    private DataManager dataManager;
    private  boolean needUpdateFengMing = true;
    private  boolean needUpdateLight = true;

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
        mActivity = this;
        shidu = findViewById(R.id.shidu);
        shidu.setText("");
        wendu = findViewById(R.id.wendu);
        wendu.setText("" );
        switchFengming = (Switch) findViewById(R.id.switchFengming);
        switchDeng = (Switch) findViewById(R.id.switchDeng);
        dataManager = DataManager.getInstance(this);
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
//                                needUpdateFengMing = false;
//                                needUpdateLight = false;
                                switchFengming.setChecked(deng ==1);
                                switchDeng.setChecked(fengming ==1);
                                switchFengming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if(!needUpdateFengMing){
                                            needUpdateFengMing = true;
                                            return;
                                        }
                                        //控制开关字体颜色
                                        if (b) {
                                            fengming = 1;
                                        }else {
                                            fengming = 0;
                                        }
                                        setControl();
                                    }
                                });
                                switchDeng.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                    @Override

                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if(!needUpdateLight){
                                            needUpdateLight = true;
                                            return;
                                        }
                                        if (b) {
                                            deng = 1;
                                        }else {
                                            deng = 0;
                                        }
                                        setControl();

                                    }

                                });
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
                                DataBean dataBean = gson.fromJson(result, DataBean.class);
                                JsonObject jsonObject = new JsonObject();
                                Log.d("result ===  ", dataBean.getWendu()+"    "+ dataBean.getShidu());
//                                double parseDouble = Double.parseDouble(tempResult);
                                wendu.setText(""+ dataBean.getWendu() );
                                shidu.setText("" + dataBean.getShidu());
                                long timecurrentTimeMillis = System.currentTimeMillis();
                                String time = stampToDate(timecurrentTimeMillis+"");
                                dataBean.setTime(time);
//                                if(dataBean.getShidu() > 0 && dataBean.getWendu() >0){
                                    dataManager.addData(dataBean);
                                try {
                                    dataManager.saveDatas();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
//                                }
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        deng = 0;
        fengming = 0;
        setControl();
    }


    public void onBtnHistoryClivk(View view) {
        startActivity(new Intent(MainActivity.this,HistoryActivity.class));
    }

    /*
     * 将时间戳转换为时间
     *
     * s就是时间戳
     */

    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}