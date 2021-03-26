package com.johnson.fingerprintpunch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.johnson.fingerprintpunch.adapter.DakaAdapter;
import com.johnson.fingerprintpunch.R;
import com.johnson.fingerprintpunch.manager.StudentManager;
import com.johnson.fingerprintpunch.bean.Student;
import com.johnson.fingerprintpunch.bean.UserBean;
import com.johnson.fingerprintpunch.service.APIService;
import com.johnson.fingerprintpunch.service.DakaService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "Johnson";
    // fruitList用于存储数据
    private List<String> fruitList=new ArrayList<>();
    private ListView listView;
    private TextView dakaNum;
    private TextView scoreEnglish;
    private TextView scoreMath;
    private TextView scoreAuto;
    private TextView scorePolitical;
    private View scoreDetail;
    private static final int SEND_SMS = 100;
    /**发送与接收的广播**/
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    private Timer timer;
    private TextView username;
    private TextView userId;
    {
        timer = new Timer(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // 先拿到数据并放在适配器上
        listView = findViewById(R.id.lv_daka);
        dakaNum = findViewById(R.id.daka_num);
        scoreDetail = findViewById(R.id.score_detail);
        scoreEnglish = findViewById(R.id.tv_score_english);
        scoreAuto = findViewById(R.id.tv_score_auto);
        scoreMath = findViewById(R.id.tv_score_math);
        scorePolitical = findViewById(R.id.tv_score_zhengzhi);
        username = findViewById(R.id.name);
        userId = findViewById(R.id.id);

        listView.setVisibility(View.GONE);
        scoreDetail.setVisibility(View.GONE);
        getDakaData(); //初始化打卡数据
        updateScore();
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
                updateUserData();
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
                updateUserData();
            }
        }, 1000, 1000);
    }

    public void refreshData(){
        getDakaData();
        updateScore();
    }

    private void updateScore() {
        Student student = StudentManager.getStudent();
        if(student==null){
            return;
        }
        float autoCtrl = student.getAutoctrl();
        float english = student.getEnglish();
        float math = student.getMath();
        float political = student.getPolitical();
        String name = student.getName();
        int id = student.getId();
//        if(autoCtrl > 0 && english >0 && math >0 && political>0){
            scoreEnglish.setText(String.valueOf(english));
            scoreAuto.setText(String.valueOf(autoCtrl));
            scorePolitical.setText(String.valueOf(political));
            scoreMath.setText(String.valueOf(math));
//        }
        int dakanum = student.getDaka_num();
        dakaNum.setText(String.valueOf(dakanum)+"/15");
        username.setText(name);
        userId.setText(String.valueOf(id));
    }

    private void getDakaData() {
        if(timer==null){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8533/").build();
        DakaService dakaService = retrofit.create(DakaService.class);
        Student student = StudentManager.getStudent();
        if(student==null){
            return;
        }
        Call<String> dakaCall =dakaService.getData(student.getId());
        dakaCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                fruitList.clear();
                try {
                    String body1 = response.body();
                    body = body1;
                    JSONArray jsonArray = new JSONArray(body);
                    Student student = StudentManager.getStudent();
                    student.setDaka_num(jsonArray.length());
                    dakaNum.setText(String.valueOf(jsonArray.length())+"/15");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // JSON数组里面的具体-JSON对象
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.optString("name", null);
                        String time = jsonObject.optString("time", "");
                        fruitList.add(time);
                        // 日志打印结果：
                        Log.d(TAG, "analyzeJSONArray1 解析的结果：name" + name + " time:" + time);
                    }
                    DakaAdapter adapter=new DakaAdapter(DetailActivity.this,R.layout.daka_item,fruitList);
                    listView.setAdapter(adapter);
                } catch (Exception e) {
                    listView.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8533/").build();
        APIService apiService = retrofit.create(APIService.class);
        Student student = StudentManager.getStudent();
        Call<String> apiCall =apiService.queryUser(student.getId());
        apiCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String body1 = response.body();
                    body = body1;
                    Gson gson = new Gson();
                    UserBean userBean = gson.fromJson(body, UserBean.class);
                    Student student = gson.fromJson(body, Student.class);
                    Log.d("Johnson",userBean.toString());
                    if(userBean.getErrorno() == 1){
                        StudentManager.setStudent(student);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateScore();
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson","=========onFailure=========2");
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onInputScoreClick(View view) {
        Student student = StudentManager.getStudent();
        int dakaNum = student.getDaka_num();
        if(dakaNum<12){
            Toast.makeText(this, "缺勤三次及三次以上不能录入成绩", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, InputActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onDakaDetailClick(View view) {
        scoreDetail.setVisibility(View.GONE);
        if(listView.getVisibility()== View.VISIBLE){
            listView.setVisibility(View.GONE);
        }else{
            getDakaData();
            listView.setVisibility(View.VISIBLE);
        }
    }

    public void onLookScoreClick(View view) {
        listView.setVisibility(View.GONE);
        if(scoreDetail.getVisibility()== View.VISIBLE){
            scoreDetail.setVisibility(View.GONE);
        }else{
            scoreDetail.setVisibility(View.VISIBLE);
        }
    }

    public void onSendScoreClick(View view) {
        requestPermission();
        String msg = getMsg();
        Log.d(TAG,msg);
    }

    private String getMsg() {
        Student student = StudentManager.getStudent();
        float autoCtrl = student.getAutoctrl();
        float english = student.getEnglish();
        float math = student.getMath();
        float political = student.getPolitical();
        int dakaNum = student.getDaka_num();
        String name = student.getName();
//        if(autoCtrl > 0 && english >0 && math >0 && political>0){
        scoreEnglish.setText(String.valueOf(english));
        scoreAuto.setText(String.valueOf(autoCtrl));
        scorePolitical.setText(String.valueOf(political));
        scoreMath.setText(String.valueOf(math));
//        }

        float sum = autoCtrl + english+math+political;
//        String msg = name+"的家长，您好！"+name+"同学此次考试的成绩如下：英语 "
//                +english+" 高数："+math+" 政治："+political+" 自动化控制原理："+autoCtrl+"。总得分为："+sum;
        String msg = "您好！"+name+"考试成绩如下：英语 "
                +english+" 高数："+math+" 政治："+political+" 自动化控制原理："+autoCtrl+"。总计打卡:"+dakaNum+"次";
        return msg;
    }


    private void requestPermission() {
        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS);
                return;
            } else {
                sendSMSS();
                //已有权限
            }
        } else {
            //API 版本在23以下
        }
    }

    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSS();
                } else {
                    // Permission Denied
                    Toast.makeText(DetailActivity.this, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //发送短信
    private void sendSMSS() {
        Student student = StudentManager.getStudent();
        String phone = student.getTel();
        String content = getMsg();
        Log.d(TAG,phone+ "    " + content);
        if (!"".equals(content) && !"".equals(phone)) {
            SmsManager manager = SmsManager.getDefault();
            Intent sentIntent = new Intent(SENT_SMS_ACTION);
            PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
                    0);

            // create the deilverIntent parameter
            Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
            PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,
                    deliverIntent, 0);
            List<String> divideContents = manager.divideMessage(content);
            Log.e("Johnson", "divide into " + divideContents.size() + " parts.");
            for (String text : divideContents) {
                Log.i("Johnson", text + "(length:" + text.length() + ")");
                // s
                manager.sendTextMessage(phone, null, text, sentPI, deliverPI);
            }
//            manager.sendTextMessage(phone, null, content, sentPI, deliverPI);
            // 注册广播 发送消息
            registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
            registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION));
//            Toast.makeText(DetailActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "手机号或内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private BroadcastReceiver sendMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //判断短信是否发送成功
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(context, "发送失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //表示对方成功收到短信
            Toast.makeText(context, "对方接收成功",Toast.LENGTH_LONG).show();
        }
    };
}