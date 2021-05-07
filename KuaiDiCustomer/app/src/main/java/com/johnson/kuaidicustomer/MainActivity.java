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
    private Button btnReceive;
    private Button btnSubmit;

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
        btnReceive = findViewById(R.id.btn_Receive);
        btnSubmit = findViewById(R.id.btn_submit);
        tvTips = findViewById(R.id.tv_tips);
        layoutCreate = findViewById(R.id.layout_create);
        layoutWeightInfo = findViewById(R.id.layout_weight_info);
        tvWeight = findViewById(R.id.tv_weight);
        tvFee = findViewById(R.id.tv_fee);
        tvTips.setVisibility(View.INVISIBLE);

        btnTake.setVisibility(View.GONE);
        btnReceive.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);

        layoutCreate.setVisibility(View.GONE);
        layoutWeightInfo.setVisibility(View.GONE);

    }

    public void onOpenDoor(View view) {
        openOrCloseDoor (1,"打开柜门");
    }

    public void onSubmitinfo(View view) {
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
                        //提交信息失败
                        Toast.makeText(MainActivity.this, "提交失败，请重试", Toast.LENGTH_SHORT).show();
                        btnClose.setEnabled(true);
                        tvTips.setVisibility(View.VISIBLE);
                        tvTips.setText("提交失败，请重试");
                    } else {
                        //创建订单成功
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
                Toast.makeText(MainActivity.this, "提交信息失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onCloseDoor(View view) {
        openOrCloseDoor (0,"提交信息");
    }

    private void openOrCloseDoor(int i,String order) {
        Call<String> apiCall = apiService.updateDoor(i);
        apiCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                openWeightFeeDialog();
                if (i==1) {
                    Toast.makeText(MainActivity.this, "打开柜门成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "关闭柜门成功", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (i==1) {
                    Toast.makeText(MainActivity.this, "打开柜门失败，请重试", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "关闭柜门失败，请重试", Toast.LENGTH_SHORT).show();
                }
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
        btnReceive.setVisibility(View.GONE);
        btnTake.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        btnClose.setClickable(true);
        btnClose.setEnabled(true);
        switch (status){
            case 0:
            case 7: //结束
                btnSubmit.setVisibility(View.VISIBLE);
                layoutCreate.setVisibility(View.VISIBLE);
                layoutWeightInfo.setVisibility(View.GONE);
                btnTake.setVisibility(View.GONE);
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
                tvTips.setText("您已拒绝下单，等待快递员放置快递");
                break;
            case 5: //开始配送
                updateCreateView();
                tvTips.setText("快递已开始配送，等待快递送达");
                break;
            case 6: //确认送达
                updateCreateView();
                btnOpen.setClickable(true);
                btnOpen.setEnabled(true);
                btnReceive.setVisibility(View.VISIBLE);
                tvTips.setText("快递已送达，请确认收货");
                break;
            case 8:  //已退还快递
                updateCreateView();
                btnOpen.setClickable(true);
                btnOpen.setEnabled(true);
                btnTake.setVisibility(View.VISIBLE);
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


    public void onTakeExpress(View view) {
        updateExpressStatus(7,"取走快递");
//        openOrCloseDoor(0,"取走快递");
    }

    public void onReceiveExpress(View view) {
        updateExpressStatus(7,"确认收货");
//        openOrCloseDoor(0,"确认收货");
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
                            }else if(order == "取走快递") {
                                Toast.makeText(MainActivity.this, "快递已取，结束配送", Toast.LENGTH_SHORT).show();
                            }else if(order == "确认收货") {
                                Toast.makeText(MainActivity.this, "确认收货成功，订单已完成", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            if (order == "拒绝下单") {
                                Toast.makeText(MainActivity.this, "拒绝下单失败，请重试", Toast.LENGTH_SHORT).show();
                            }else if(order == "支付") {
                                Toast.makeText(MainActivity.this, "支付失败，请重试", Toast.LENGTH_SHORT).show();
                            }else if(order == "取走快递") {
                                Toast.makeText(MainActivity.this, "更新状态失败，请重试", Toast.LENGTH_SHORT).show();
                            }else if(order == "确认收货") {
                                Toast.makeText(MainActivity.this, "确认收货失败，请重试", Toast.LENGTH_SHORT).show();
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
}