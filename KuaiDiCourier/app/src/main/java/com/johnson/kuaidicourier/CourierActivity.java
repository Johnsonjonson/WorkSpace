package com.johnson.kuaidicourier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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

import java.io.IOException;
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
    private Retrofit retrofit;
    private APIService apiService;
    private Button btnFahuo;
    private Button btnArrive;
    private AlertDialog backDialog;

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
        setContentView(R.layout.activity_courier);
        initView();

    }

    private void initView() {
        btnRecorders = findViewById(R.id.btn_recorders);
        btnCourierOpen = findViewById(R.id.btn_courier_open);
        btnFahuo = findViewById(R.id.btn_fahuo);
        btnArrive = findViewById(R.id.btn_arrive);
        tvTips = findViewById(R.id.tv_courier_tips);
        layoutStart = findViewById(R.id.layout_start);
        tvTips.setVisibility(View.INVISIBLE);
        btnCourierOpen.setEnabled(false);
        btnRecorders.setClickable(false);
        layoutStart.setVisibility(View.GONE);
        btnFahuo.setVisibility(View.GONE);
        btnArrive.setVisibility(View.GONE);
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
        openOrCloseDoor(1);
    }

    private void openOrCloseDoor(int i) {
        Call<String> apiCall = apiService.updateDoor(i);
        apiCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                openWeightFeeDialog();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(CourierActivity.this, "打开柜门失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openWeightFeeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 获取布局
        View view2 = View.inflate(this, R.layout.weight_fee_layout, null);
        // 获取布局中的控件
        EditText etWeight = view2.findViewById(R.id.et_weight);
        EditText etFee = view2.findViewById(R.id.et_fee);
        Button btnConfirm = view2.findViewById(R.id.btn_confirm);
        // 设置参数
        builder.setTitle("重量和费用").setIcon(R.drawable.ic_launcher_background).setView(view2);
        // 创建对话框
        AlertDialog detailDialog = builder.show();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String weight = etWeight.getText().toString();
                String fee = etFee.getText().toString();
                if (weight.isEmpty()){
                    Toast.makeText(CourierActivity.this, "请输入重量", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (fee.isEmpty()){
                    Toast.makeText(CourierActivity.this, "请输入费用", Toast.LENGTH_SHORT).show();
                    return;
                }
                detailDialog.dismiss();
                updateExpressWeightAndFee(weight,fee);
            }
        });
    }

    private void updateExpressWeightAndFee(String weight, String fee) {
        if (curExpressInfo != null){
            int id = curExpressInfo.getId();
            Call<String> apiCall = apiService.updateWieghtAndFee(weight,fee,id);
            apiCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String body = "";
                    try {
                        String express = response.body();
                        body = express;

                        JSONObject jsParam = new JSONObject(body);
                        int errorno = jsParam.optInt("errorno", 0);
                        if(errorno ==1 ){
                            Toast.makeText(CourierActivity.this, "已设置重量和费用信息，等待用户支付", Toast.LENGTH_SHORT).show();
                            tvTips.setText("已设置重量和费用信息，等待用户支付");
                        }else{
                            Toast.makeText(CourierActivity.this, "设置重量和费用信息失败，请重试", Toast.LENGTH_SHORT).show();
                        }
//                        int status = jsParam.optInt("status", 0);
//                        Gson gson  = new Gson();
//                        curExpressInfo = gson.fromJson(body, Express.class);
//                        updateView(status);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("Johnson", "=======提交信息成功==onFailure=========2");
                }
            });
        }
    }

    public void onOpenClick(View view) {
        openOrCloseDoor(1);
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
                    int errorno = jsParam.optInt("errorno", 0);
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
            }
        });
    }

    private void updateView(int status) {
        btnFahuo.setVisibility(View.GONE);
        btnArrive.setVisibility(View.GONE);
        switch (status){
            case 0:
            case 7: //结束
                layoutStart.setVisibility(View.VISIBLE);
                tvTips.setVisibility(View.VISIBLE);
                btnCourierOpen.setEnabled(false);
                btnCourierOpen.setClickable(false);
                tvTips.setText("暂无快递");
                break;
            case 1:   //正在设置费用
                layoutStart.setVisibility(View.VISIBLE);
                btnCourierOpen.setEnabled(true);
                btnCourierOpen.setClickable(true);
//                btnC
                tvTips.setVisibility(View.VISIBLE);
                tvTips.setText("有新的快递，请录入重量和价格");
                break;
            case 2:  //已设置设置费用
                layoutStart.setVisibility(View.VISIBLE);
                tvTips.setVisibility(View.VISIBLE);
                tvTips.setText("已设置重量和费用信息，等待用户支付");
                btnCourierOpen.setEnabled(false);
                btnCourierOpen.setClickable(false);
//                updateCreateView();
                break;
            case 3:  //已付款
                btnFahuo.setVisibility(View.VISIBLE);
                tvTips.setText("用户已付款，请发货");
                break;
            case 4: //拒绝下单
                tvTips.setText("用户拒绝下单，将物品放置快递柜");
                showBackExpressDialog();
                break;
            case 5: //开始配送
                btnArrive.setVisibility(View.VISIBLE);
                tvTips.setText("快递正在配送");
                break;
            case 6: //确认送达
                tvTips.setText("快递已送达，等待用户确认收货");
                break;
            case 8: ///已退还快递
                layoutStart.setVisibility(View.VISIBLE);
                tvTips.setVisibility(View.VISIBLE);
                btnCourierOpen.setEnabled(false);
                btnCourierOpen.setClickable(false);
                tvTips.setText("快递已退还，等待用户取货");
                break;
        }
    }

    private void showBackExpressDialog() {
        if (backDialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // 获取布局
            View view2 = View.inflate(this, R.layout.back_express_layout, null);
            // 获取布局中的控件
            Button btnConfirm = view2.findViewById(R.id.btn_confirm);
            // 设置参数
            builder.setTitle("退还快递").setIcon(R.drawable.ic_launcher_background).setView(view2);
            // 创建对话框
            backDialog = builder.show();

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    updateExpressStatus(8,"退还快递");
                    backDialog.dismiss();
                }
            });
        }
        backDialog.show();
    }

    public void onFaHuonClick(View view) {
        updateExpressStatus(5,"开始配送");
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
                            if (order == "开始配送") {
                                Toast.makeText(CourierActivity.this, "更新状态成功，开始配送", Toast.LENGTH_SHORT).show();
                            }else if(order == "确认送达") {
                                Toast.makeText(CourierActivity.this, "更新状态成功，已确认送达", Toast.LENGTH_SHORT).show();
                            }else if(order == "退还快递") {
                                Toast.makeText(CourierActivity.this, "快递已退还，等待用户取货", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            if (order == "开始配送") {
                                Toast.makeText(CourierActivity.this, "更新状态失败，请重试", Toast.LENGTH_SHORT).show();
                            }else if(order == "确认送达") {
                                Toast.makeText(CourierActivity.this, "更新状态失败，请重试", Toast.LENGTH_SHORT).show();
                            }else if(order == "确认送达") {
                                Toast.makeText(CourierActivity.this, "快递退还失败，请重试", Toast.LENGTH_SHORT).show();
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

    public void onArriveClick(View view) {
        updateExpressStatus(6,"确认送达");
    }
}