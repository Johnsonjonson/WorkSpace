package com.johnson.kuaidicustomer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.johnson.kuaidicustomer.bean.Express;
import com.johnson.kuaidicustomer.service.APIService;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CourierActivity extends AppCompatActivity {
    private Timer timer;
    private Express curExpressInfo;
    private Button btnRecorders;
    private Button btnCourierOpen;
    private LinearLayout layoutStart;
    private TextView tvTips;

    {
        timer = new Timer(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        initView();

    }

    private void initView() {
        btnRecorders = findViewById(R.id.btn_recorders);
        btnCourierOpen = findViewById(R.id.btn_courier_open);
        tvTips = findViewById(R.id.tv_courier_tips);
        layoutStart = findViewById(R.id.layout_start);
        tvTips.setVisibility(View.INVISIBLE);
        btnCourierOpen.setEnabled(false);
        btnRecorders.setClickable(false);
        layoutStart.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                requestExpressInfo();
            }
        }, 1000, 1000);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null ){
            timer.cancel();
            timer= null;
        }
    }

    public void onRecordersClick(View view) {
    }

    public void onOpenClick(View view) {
    }

    private void requestExpressInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8547/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> bookParkCall = apiService.getLastExpress();
        bookParkCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String express = response.body();
                    body = express;

                    JSONObject jsParam = new JSONObject(body);
                    int errorno = jsParam.optInt("errorno", 0);
                    int status = jsParam.optInt("status", 0);
//                    int status = jsParam.optInt("status", 0);
                    Gson gson  = new Gson();
                    curExpressInfo = gson.fromJson(body, Express.class);
                    updateView(status);
//                    if (errorno ==1) {
//                        Gson gson  = new Gson();
//                        curExpressInfo = gson.fromJson(body, Express.class);
//                        updateView(status);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson", "=======提交信息成功==onFailure=========2");
//                Toast.makeText(MainActivity.this, "提交信息成功，等待快递员录入价格", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateView(int status) {
        switch (status){
            case 0:
            case 8: //结束
                layoutStart.setVisibility(View.VISIBLE);
//                btnClose.setClickable(true);
//                btnClose.setEnabled(true);
//                if (curExpressInfo!=null) {
//                    etStart.setText(curExpressInfo.getStart());
//                    etEnd.setText(curExpressInfo.getEnd());
//                    etPhone.setText(curExpressInfo.getPhone());
//                    etName.setText(curExpressInfo.getName());
//                }
                break;
            case 1:   //正在下单
            case 2:  //正在设置费用
                 layoutStart.setVisibility(View.VISIBLE);
                 btnCourierOpen.setEnabled(true);
                 btnRecorders.setClickable(true);
//                updateCreateView();
                break;
            case 3:  //已设置设置费用
                break;
            case 4:  //确认下单
                break;
            case 5: //拒绝下单
                break;
            case 6: //开始配送
                break;
            case 7: //确认送达
                break;
        }
    }
}