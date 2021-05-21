package com.johnson.raspberrypihealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

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
            }
        });
    }

    private void updateHealthInfo(HealthData healthDataInfo) {
        tvHeartRate.setText("Heart Rate："+healthDataInfo.getHeart());
        tvBloodPressure.setText("Blood Pressure：" +healthDataInfo.getBlood());
        tvTemperature.setText("Temperature：" + healthDataInfo.getTemp());
    }

}