package com.johnson.securitymonitoringsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.johnson.securitymonitoringsystem.bean.MonitorData;
import com.johnson.securitymonitoringsystem.service.APIService;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView tvInfraRed;
    private TextView tvFire;
    private TextView tvTemp;
    private Switch cameraSwitch;


    private String BASE_URL = "http://129.204.232.210:8554/";

    private Timer timer;
    private boolean isShowAlert;
    private Retrofit retrofit;
    private APIService apiService;
    private boolean isGetData;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;


    {
        timer = new Timer(true);
        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BASE_URL).build();
        apiService = retrofit.create(APIService.class);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder = new AlertDialog.Builder(this);
        // 获取布局
        builder.setTitle("Warning⚠️");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(true);
        initView();
        requestInfoData();
        isGetData = false;
        getSwitch();
    }

    private void initView() {
        tvInfraRed = findViewById(R.id.infra_red);
        tvTemp = findViewById(R.id.tv_temp);
        tvFire = findViewById(R.id.tv_fire);
        cameraSwitch = findViewById(R.id.camera_switch);
        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String msg = "0";
                if(b){
                    msg = "1";
                }
                if(!isGetData)
                    updateSwitch(msg);
            }
        });
    }

    private void updateSwitch(String msg) {
        Call<String> allCall = apiService.setSwitch(msg);
        allCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String body = response.body();
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
    private void getSwitch() {
        Call<String> allCall = apiService.getSwitch();
        allCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String body = response.body();
                    isGetData = true;
                    cameraSwitch.setChecked(Integer.parseInt(body)==1);
                    isGetData = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
                requestInfoData();
            }
        }, 0, 1000);
    }



    private void requestInfoData() {
        Call<String> allCall = apiService.getInfo();
        allCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String body = response.body();
                    JSONObject monitorInfo = new JSONObject(body);
                    Gson gson  = new Gson();
                    MonitorData monitorDataInfo = gson.fromJson(monitorInfo.toString(), MonitorData.class);
                    updateHealthInfo(monitorDataInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
    private void updateHealthInfo(MonitorData monitorDataInfo) {
        int fire = monitorDataInfo.getFire();
        int red = monitorDataInfo.getRed();
        double temp = monitorDataInfo.getTemp();

        if(red ==1){
            tvInfraRed.setText("Someone detected");
            if(!isShowAlert){
                showAlertDialog("Someone detected");
            }
        }else{
            tvInfraRed.setText("No one was detected");
        }

        if(fire ==1){
            tvFire.setText("Toxic");
            if(!isShowAlert){
                showAlertDialog("Toxic");
            }
        }else{
            tvFire.setText("non-toxic");
        }
        tvTemp.setText(temp+"°C");
    }

    private void showAlertDialog(String msg){
        // 获取布局
        builder.setMessage(msg);
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
        if(alertDialog==null) {
            alertDialog = builder.create();
            //对话框显示的监听事件
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    //                Log.e(TAG, "对话框显示了");
                    isShowAlert = true;
                }
            });
        }
        //对话框消失的监听事件
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
////                Log.e(TAG, "对话框消失了");
//                isShowAlert = false;
//            }
//        });
        alertDialog.show();
    }

}