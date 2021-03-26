package com.johnson.fingerprintpunch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.johnson.fingerprintpunch.R;
import com.johnson.fingerprintpunch.bean.Student;
import com.johnson.fingerprintpunch.bean.UserBean;
import com.johnson.fingerprintpunch.service.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 注册
 */
public class AddStudentActivity extends AppCompatActivity {

    private String username;
    private String tel;
    private String sex;
    private EditText userNameEdt;
    private EditText telEdt;
    private RadioGroup sexRegister;
    private RadioButton sexNan;
    private RadioButton sexNv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        userNameEdt = (EditText)findViewById(R.id.usernameAdd);
        telEdt = (EditText)findViewById(R.id.telAdd);
        sexRegister = (RadioGroup)findViewById(R.id.sexAdd);
        sexNan = (RadioButton)findViewById(R.id.add_nan);
        sexNv = (RadioButton)findViewById(R.id.add_nv);
        sexRegister.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("Johnson","===========" + i + sexNan.isChecked() +"    "+ sexNv.isChecked());
            }
        });
        sexNan.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onAddClick(View view) {
        username = String.valueOf(userNameEdt.getText());
        tel = String.valueOf(telEdt.getText());
        if("".equals(username)){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(tel)){
            Toast.makeText(this, "电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sexNan.isChecked()){
            sex = "male";
        }else if (sexNv.isChecked()){
            sex = "female";
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8533/").build();
        APIService apiService = retrofit.create(APIService.class);
        Log.d("Johnson",username   + "    " )      ;
        Call<String> addCall =apiService.add(username,tel);
        addCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String body1 = response.body();
                    body = body1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                Log.d("Johnson","body = " + body );
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(body, UserBean.class);
                Student student = gson.fromJson(body, Student.class);
                Log.d("Johnson",userBean.toString());
//                StudentManager.setStudent(student);
                Log.d("Johnson","======" + student.toString());
                if(userBean.getErrorno() == 1){
                    Toast.makeText(AddStudentActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddStudentActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                    AddStudentActivity.this.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson","=========onFailure=========2");
            }
        });
    }
}