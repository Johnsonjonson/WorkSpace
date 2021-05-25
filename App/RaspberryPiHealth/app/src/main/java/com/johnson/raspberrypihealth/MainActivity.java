package com.johnson.raspberrypihealth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.johnson.raspberrypihealth.bean.HealthData;
import com.johnson.raspberrypihealth.service.APIService;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView tvHeartRate;
    private TextView tvBloodPressure;
    private TextView tvTemperature;

    private Timer timer;
    private boolean isShowAlert;

    {
        timer = new Timer(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestInfoData();
    }

    private void initView() {
        tvHeartRate = findViewById(R.id.tv_heart_rate);
        tvBloodPressure = findViewById(R.id.tv_blood_pressure);
        tvTemperature = findViewById(R.id.tv_temperature);
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
                requestInfoData();
            }
        }, 0, 1000);
    }



    private void requestInfoData() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8553/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> allCall = apiService.getInfo();
        allCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String body = response.body();
                    JSONObject heathInfo = new JSONObject(body);
                    Gson gson  = new Gson();
                    HealthData healthDataInfo = gson.fromJson(heathInfo.toString(), HealthData.class);
                    updateHealthInfo(healthDataInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(MainActivity.this, "request fail", Toast.LENGTH_SHORT).show();











            }
        });
    }

    private double maxBlood = 140;
    private double minBlood = 90;
    private double maxTemp = 37;
    private double minTemp = 36;
    private double maxHeart= 100;
    private double minHeart= 60;

    private void updateHealthInfo(HealthData healthDataInfo) {
        double blood = healthDataInfo.getBlood();
        double heart = healthDataInfo.getHeart();
        double temp = healthDataInfo.getTemp();
        tvHeartRate.setText("Heart Rate："+heart);
        tvBloodPressure.setText("Blood Pressure：" +blood);
        tvTemperature.setText("Temperature：" + temp);
        if(isShowAlert){
            return;
        }
//        if (blood > maxBlood){
//            showAlertDialog("血压偏高");
//            return;
//        }
//
//        if(blood<minBlood){
//            showAlertDialog("血压偏低");
//            return;
//        }
//
//        if (heart > maxHeart){
//            showAlertDialog("心率偏高");
//            return;
//        }
//
//        if( heart<minHeart){
//            showAlertDialog("心率偏低");
//            return;
//        }
        if (temp > maxTemp){
            showAlertDialog("High Temperature");
            return;
        }
//        if(temp<minTemp){
//            showAlertDialog("体温偏低");
//            return;
//        }
    }

    private void showAlertDialog(String msg){
//        isShowAlert = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 获取布局
        builder.setMessage(msg);
        builder.setTitle("Warning⚠️");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(true);

        builder.setNeutralButton("Don't prompt again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isShowAlert = false;
            }
        });

//        builder.setNegativeButton("不再提示", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
        AlertDialog dialog = builder.create();

//        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        //对话框显示的监听事件
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
//                Log.e(TAG, "对话框显示了");
                isShowAlert = true;
            }
        });
        //对话框消失的监听事件
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
////                Log.e(TAG, "对话框消失了");
//                isShowAlert = false;
//            }
//        });
        dialog.show();
    }

}