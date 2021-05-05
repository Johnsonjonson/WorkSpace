package com.johnson.kuaidicourier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.johnson.kuaidicourier.bean.Express;
import com.johnson.kuaidicourier.service.APIService;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText etStart;
    private EditText etEnd;
    private EditText etPhone;
    private EditText etName;
    private Button btnOpen;
    private Button btnClose;
    private TextView tvTips;
    private Timer timer;
    private LinearLayout layoutCreate;
    private Express curExpressInfo;

    {
        timer = new Timer(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

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

    private void initView() {
        etStart = findViewById(R.id.et_start);
        etEnd = findViewById(R.id.et_end);
        etPhone = findViewById(R.id.et_phone);
        etName = findViewById(R.id.et_name);
        btnOpen = findViewById(R.id.btn_open);
        btnClose = findViewById(R.id.btn_close);
        tvTips = findViewById(R.id.tv_tips);
        layoutCreate = findViewById(R.id.layout_create);
        tvTips.setVisibility(View.INVISIBLE);

        layoutCreate.setVisibility(View.GONE);

    }

    public void onOpenDoor(View view) {
    }

    public void onCloseDoor(View view) {
        String etStartText = etStart.getText().toString();
        String etEndText = etEnd.getText().toString();
        String etPhoneText = etPhone.getText().toString();
        String etNameText = etName.getText().toString();

        if(etStartText.isEmpty()) {
            Toast.makeText(this, "请输入起点", Toast.LENGTH_SHORT).show();
            return;
        }

        if(etEndText.isEmpty()) {
            Toast.makeText(this, "请输入终点", Toast.LENGTH_SHORT).show();
            return;
        }

        if(etPhoneText.isEmpty()) {
            Toast.makeText(this, "请输入电话", Toast.LENGTH_SHORT).show();
            return;
        }

        if(etNameText.isEmpty()) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8547/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> bookParkCall = apiService.createExpress(etStartText, etEndText, etPhoneText, etNameText);
        bookParkCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String body1 = response.body();
                    body = body1;

                    JSONObject jsParam = new JSONObject(body);
                    int errorno = jsParam.optInt("errorno", 0);
                    if (errorno == 0) {
                        //预约失败
                        Toast.makeText(MainActivity.this, "提交失败，请重试", Toast.LENGTH_SHORT).show();
                        btnClose.setEnabled(true);
                        tvTips.setVisibility(View.VISIBLE);
                        tvTips.setText("提交失败，请重试");
                    } else {
                        //预约成功
                        Toast.makeText(MainActivity.this, "信息已提交成功，等待快递员录入价格", Toast.LENGTH_SHORT).show();
                        btnClose.setEnabled(false);
                        btnClose.setClickable(false);
                        tvTips.setVisibility(View.VISIBLE);
                        tvTips.setText("信息已提交成功，等待快递员录入价格");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson", "=======提交信息成功==onFailure=========2");
                Toast.makeText(MainActivity.this, "提交信息成功，等待快递员录入价格", Toast.LENGTH_SHORT).show();
            }
        });
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
                layoutCreate.setVisibility(View.VISIBLE);
                btnClose.setClickable(true);
                btnClose.setEnabled(true);
//                if (curExpressInfo!=null) {
//                    etStart.setText(curExpressInfo.getStart());
//                    etEnd.setText(curExpressInfo.getEnd());
//                    etPhone.setText(curExpressInfo.getPhone());
//                    etName.setText(curExpressInfo.getName());
//                }
                break;
            case 1:   //正在设置费用
                updateCreateView();
                break;
            case 2:  ///已设置设置费用

                break;
            case 3:  //确认下单
                break;
            case 4:  //已付款
                break;
            case 5: //拒绝下单
                break;
            case 6: //开始配送
                break;
            case 7: //确认送达
                break;
        }
    }

    private void updateCreateView() {
//        curExpressInfo.get
        if (curExpressInfo!=null) {
            etStart.setText(curExpressInfo.getStart());
            etEnd.setText(curExpressInfo.getEnd());
            etPhone.setText(curExpressInfo.getPhone());
            etName.setText(curExpressInfo.getName());
        }
        layoutCreate.setVisibility(View.VISIBLE);
        btnClose.setEnabled(false);
        btnClose.setClickable(false);
        tvTips.setVisibility(View.VISIBLE);
        tvTips.setText("信息已提交成功，等待快递员录入价格");
    }
}