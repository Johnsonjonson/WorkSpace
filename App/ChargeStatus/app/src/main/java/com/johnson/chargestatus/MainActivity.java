package com.johnson.chargestatus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.johnson.chargestatus.bean.ChargeData;
import com.johnson.chargestatus.service.APIService;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView tvCurrent;
    private TextView tvVoltage;
    private TextView tvTemperature;
    private TextView tvChargeStatus;

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
        tvCurrent = findViewById(R.id.tv_current);
        tvVoltage = findViewById(R.id.tv_voltage);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvChargeStatus = findViewById(R.id.tv_charge_status);
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
                .baseUrl("http://119.91.152.113:8553/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> allCall = apiService.getInfo();
        allCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String body = response.body();
                    JSONObject heathInfo = new JSONObject(body);
                    Gson gson  = new Gson();
                    ChargeData healthDataInfo = gson.fromJson(heathInfo.toString(), ChargeData.class);
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
    private double maxTemp = 37;

    private void updateHealthInfo(ChargeData chargeDataInfo) {
        double voltage = chargeDataInfo.getVoltage();
        double current = chargeDataInfo.getCurrent();
        double temp = chargeDataInfo.getTemp();
        double status = chargeDataInfo.getStatus();
        tvCurrent.setText("Current："+current + "A");
        tvVoltage.setText("Voltage：" +voltage + "V");
        tvTemperature.setText("Temperature：" + temp +"℃");
        tvChargeStatus.setText(status == 1?"正在充电...":"未充电");
        tvChargeStatus.setTextColor(status == 1? Color.BLUE :Color.RED);
        if(isShowAlert){
            return;
        }
        if (temp > maxTemp){
            showAlertDialog("High Temperature");
            return;
        }
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