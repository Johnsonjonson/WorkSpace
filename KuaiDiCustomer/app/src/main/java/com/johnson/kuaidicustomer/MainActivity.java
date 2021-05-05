package com.johnson.kuaidicustomer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private LinearLayout layoutWeightInfo;
    private TextView tvWeight;
    private TextView tvFee;
    private Retrofit retrofit;
    private APIService apiService;
    private Button btnTake;

    {
        timer = new Timer(true);
        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8547/").build();
        apiService = retrofit.create(APIService.class);
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
        btnTake = findViewById(R.id.btn_take);
        tvTips = findViewById(R.id.tv_tips);
        layoutCreate = findViewById(R.id.layout_create);
        btnTake.setVisibility(View.GONE);
        layoutWeightInfo = findViewById(R.id.layout_weight_info);
        tvWeight = findViewById(R.id.tv_weight);
        tvFee = findViewById(R.id.tv_fee);
        tvTips.setVisibility(View.INVISIBLE);

        layoutCreate.setVisibility(View.GONE);
        layoutWeightInfo.setVisibility(View.GONE);

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
        Call<String> bookParkCall = apiService.getLastExpress();
        bookParkCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String express = response.body();
                    body = express;

                    JSONObject jsParam = new JSONObject(body);
                    int status = jsParam.optInt("status", 0);
                    Gson gson  = new Gson();
                    curExpressInfo = gson.fromJson(body, Express.class);
                    updateView(status);
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
            case 7: //结束
                layoutCreate.setVisibility(View.VISIBLE);
                layoutWeightInfo.setVisibility(View.GONE);
                btnTake.setVisibility(View.GONE);
                btnClose.setClickable(true);
                btnClose.setEnabled(true);
                btnOpen.setClickable(true);
                btnOpen.setEnabled(true);
                tvTips.setText("");
                break;
            case 1:   //正在设置费用
                updateCreateView();
                tvTips.setText("信息已提交成功，等待快递员录入价格");
                break;
            case 2:  //已设置设置费用
                updateWeightInfo();
                tvTips.setText("信息已提交成功，等待快递员录入价格");
                break;
            case 3:  //已付款
                updateCreateView();
                tvTips.setText("已付款，等待快递员配送");
                break;
            case 4:  //拒绝下单
                updateCreateView();
                btnTake.setVisibility(View.VISIBLE);
                tvTips.setText("您已拒绝下单，等待快递员放置快递");
                break;
            case 5: //开始配送
                break;
            case 6: //确认送达
                break;
            case 8:  //已退还快递
                btnOpen.setClickable(false);
                btnOpen.setEnabled(false);
                tvTips.setText("快递已退还，请取走快递");
                break;
        }
    }

    private void updateWeightInfo() {
        if (curExpressInfo!=null) {
            tvWeight.setText(curExpressInfo.getWeight() + "");
            tvFee.setText(curExpressInfo.getFee() + "");
        }
        layoutCreate.setVisibility(View.GONE);
        layoutWeightInfo.setVisibility(View.VISIBLE);
    }

    private void updateCreateView() {
//        curExpressInfo.get
        if (curExpressInfo!=null) {
            etStart.setText(curExpressInfo.getStart());
            etEnd.setText(curExpressInfo.getEnd());
            etPhone.setText(curExpressInfo.getPhone());
            etName.setText(curExpressInfo.getName());
        }
        layoutWeightInfo.setVisibility(View.GONE);
        layoutCreate.setVisibility(View.VISIBLE);
        btnClose.setEnabled(false);
        btnClose.setClickable(false);
        btnOpen.setClickable(false);
        btnOpen.setEnabled(false);
        btnTake.setVisibility(View.GONE);
        tvTips.setVisibility(View.VISIBLE);
    }

    public void onConfirmOrderClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 获取布局
        View view2 = View.inflate(this, R.layout.pay_layout, null);
        // 获取布局中的控件
//        EditText etWeight = view2.findViewById(R.id.et_weight);
//        EditText etFee = view2.findViewById(R.id.et_fee);
        Button btnPay = view2.findViewById(R.id.btn_pay);
        // 设置参数
        builder.setTitle("二维码收款").setIcon(R.drawable.ic_launcher_background).setView(view2);
        // 创建对话框
        AlertDialog payDialog = builder.show();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay();
                payDialog.dismiss();
            }
        });
    }

    private void pay() {
        updateExpressStatus(3,"支付");
    }

    public void onRefuseOrderClick(View view) {
        updateExpressStatus(4,"拒绝下单");
    }

    public void updateExpressStatus(int status,String order){
        if(curExpressInfo != null) {
            int id = curExpressInfo.getId();
            Call<String> bookParkCall = apiService.updateStatus(id,status);
            bookParkCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String body = "";
                    try {
                        String express = response.body();
                        body = express;

                        JSONObject jsParam = new JSONObject(body);
                        int errorno = jsParam.optInt("errorno", 0);
                        if(errorno ==1){
                            if (order == "拒绝下单") {
                                Toast.makeText(MainActivity.this, "已拒绝下单，可重新填写寄件信息", Toast.LENGTH_SHORT).show();
                            }else if(order == "支付") {
                                Toast.makeText(MainActivity.this, "支付成功，等待配送", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            if (order == "拒绝下单") {
                                Toast.makeText(MainActivity.this, "拒绝下单失败，请重试", Toast.LENGTH_SHORT).show();
                            }else if(order == "支付") {
                                Toast.makeText(MainActivity.this, "支付失败，请重试", Toast.LENGTH_SHORT).show();
                            }

                        }
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
    }

    public void onTakeExpress(View view) {


    }
}