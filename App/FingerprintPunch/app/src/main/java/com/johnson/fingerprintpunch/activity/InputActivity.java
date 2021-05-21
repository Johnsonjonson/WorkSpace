package com.johnson.fingerprintpunch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.johnson.fingerprintpunch.R;
import com.johnson.fingerprintpunch.manager.StudentManager;
import com.johnson.fingerprintpunch.bean.Student;
import com.johnson.fingerprintpunch.bean.UserBean;
import com.johnson.fingerprintpunch.service.InputService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 录入成绩
 */
public class InputActivity extends AppCompatActivity {

    private EditText scoreMath;
    private EditText scoreEnglish;
    private EditText scoreZhengzhi;
    private EditText scoreAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        scoreMath = (EditText)findViewById(R.id.score_math);
        scoreEnglish = (EditText)findViewById(R.id.score_english);
        scoreZhengzhi = (EditText)findViewById(R.id.score_zhengzhi);
        scoreAuto = (EditText)findViewById(R.id.score_auto);
    }

    public void onCommitClick(View view) {
        String mathScore = scoreMath.getText().toString();
        String englishScore = scoreEnglish.getText().toString();
        String zhengzhiScore = scoreZhengzhi.getText().toString();
        String autoScore = scoreAuto.getText().toString();
        if("".equals(mathScore)
        || "".equals(englishScore)
        || "".equals(zhengzhiScore)
        || "".equals(autoScore)){
            Toast.makeText(this,"分数不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8533/").build();
        InputService inputService = retrofit.create(InputService.class);


        float math = Float.valueOf(mathScore);
        float english = Float.valueOf(englishScore);
        float zhengzhi = Float.valueOf(zhengzhiScore);
        float auto = Float.valueOf(autoScore);

        Log.d("Johnson",mathScore   + "    "  + englishScore+ "    "  + zhengzhiScore+ "    "  + autoScore)      ;
        Student stu = StudentManager.getStudent();
        int id = stu.getId();
        String name = stu.getName();
        Call<String> registCall =inputService.input(id,name,math,english,zhengzhi,auto);
        registCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String body1 = response.body();
                    body = body1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(body, UserBean.class);
                Student student = gson.fromJson(body, Student.class);
                Log.d("Johnson",userBean.toString());
                StudentManager.setStudent(student);
                if(userBean.getErrorno() == 1){
                    Toast.makeText(InputActivity.this, "分数录入成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InputActivity.this, DetailActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                    InputActivity.this.startActivity(intent);
                }else{
                    Toast.makeText(InputActivity.this, "分数录入失败", Toast.LENGTH_SHORT).show();
                }
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                Log.d("Johnson","body = " + body );
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson","=========onFailure=========2");
                Toast.makeText(InputActivity.this, "分数录入失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}