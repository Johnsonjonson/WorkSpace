package com.johnson.responder;

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

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private boolean isShowAnswer = false;
    private boolean isShowCreate = true;
    private String title;
    private String answer;
    private Button btnCreate;
    private LinearLayout subjectLayout;
    private TextView tvTitle;
    private TextView tvAnswer;
    private Button btnShowHide;
    private Timer subjectTimer;
    private TextView tvTime;
    private int remainTime;
    private static final int count_down = 30;
    public static final String BASE_URL = "http://129.204.232.210:8545/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiView();
        updateView();
    }

    private void intiView() {
        btnCreate = findViewById(R.id.btn_create);
        btnShowHide = findViewById(R.id.btn_show_hide);
        subjectLayout = findViewById(R.id.subject_layout);
        tvTitle = findViewById(R.id.tv_title);
        tvAnswer = findViewById(R.id.tv_answer);
        tvTime = findViewById(R.id.tv_time);
    }

    public void onCreateClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 获取布局
        View view2 = View.inflate(this, R.layout.create_detail_layout, null);
        // 获取布局中的控件
        EditText etAnswer = view2.findViewById(R.id.et_answer);
        EditText etTitle = view2.findViewById(R.id.et_title);
        Button btnConfirm = view2.findViewById(R.id.btn_confirm);
        // 设置参数
        builder.setTitle("创建题目").setIcon(R.drawable.ic_launcher_background).setView(view2);
        // 创建对话框
        AlertDialog detailDialog = builder.show();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                detailDialog.dismiss();
                // 填写信息
                title = etTitle.getText().toString();
                if(title == null || "".equals(title)){
                    Toast.makeText(MainActivity.this, "请输入题目", Toast.LENGTH_SHORT).show();
                    return;
                }
                answer = etAnswer.getText().toString();
                if(answer == null || "".equals(answer)){
                    Toast.makeText(MainActivity.this, "请输入答案", Toast.LENGTH_SHORT).show();
                    return;
                }
                resetIndex();
            }
        });
    }

    private void startSubjectTimer() {
        if (subjectTimer != null ){
            subjectTimer.cancel();
            subjectTimer= null;
        }
        remainTime = count_down;
        subjectTimer = new Timer(true);
        subjectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestData();
                        if (remainTime>0) {
                            tvTime.setText(remainTime + "s");
                            remainTime--;
                        }else{
                            reset();
                        }
                    }
                });
            }
        }, 0, 1000);

    }

    private void requestData() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BASE_URL).build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> apiCall = apiService.getIndex();
        apiCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String body1 = response.body();
                    JSONObject jsParam = new JSONObject(body1);
                    int index = jsParam.optInt("index", 0);
                    updateStatus(index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetIndex() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BASE_URL).build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> apiCall = apiService.setIndex("0");
        apiCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                onCreateSubjectScuccess(title,answer);
                startSubjectTimer();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatus(int index) {
        if(index>0){
            reset();
            Toast.makeText(this, index+"号抢答成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void reset() {
        stopSubjectTimer();
        isShowCreate = true;
        isShowAnswer = false;
        updateView();
        updateAnswerVisible();
    }


    private void stopSubjectTimer() {
        if (subjectTimer != null ){
            subjectTimer.cancel();
            subjectTimer= null;
        }
    }


    public void onCreateSubjectScuccess(String title,String answer){
        isShowCreate = false;
        isShowAnswer = false;
        tvAnswer.setText(answer);
        tvTitle.setText(title);
        updateView();
        updateAnswerVisible();
    }

    public void updateAnswerVisible(){
        if(isShowAnswer){
            tvAnswer.setText(answer);
            btnShowHide.setText("隐藏答案");
        }else{
            tvAnswer.setText("******");
            btnShowHide.setText("显示答案");
        }
    }

    public void onBtnShowClick(View view) {
        isShowAnswer = ! isShowAnswer;
        updateAnswerVisible();
    }

    public void updateView(){
        if(isShowCreate){
            btnCreate.setVisibility(View.VISIBLE);
            subjectLayout.setVisibility(View.GONE);
        }else{
            btnCreate.setVisibility(View.GONE);
            subjectLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subjectTimer != null ){
            subjectTimer.cancel();
            subjectTimer= null;
        }
    }
}