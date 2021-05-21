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
import com.johnson.fingerprintpunch.bean.Teacher;
import com.johnson.fingerprintpunch.manager.StudentManager;
import com.johnson.fingerprintpunch.bean.Student;
import com.johnson.fingerprintpunch.bean.UserBean;
import com.johnson.fingerprintpunch.manager.TeacherManager;
import com.johnson.fingerprintpunch.service.RegisterService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {

    private String username;
    private String pwd;
    private String tel;
    private String sex;
    private EditText userNameEdt;
    private EditText pwdEdt;
    private EditText telEdt;
    private RadioGroup sexRegister;
    private RadioButton sexNan;
    private RadioButton sexNv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userNameEdt = (EditText)findViewById(R.id.usernameRegister);
        pwdEdt = (EditText)findViewById(R.id.passwordRegister);
        telEdt = (EditText)findViewById(R.id.telRegister);
        sexRegister = (RadioGroup)findViewById(R.id.sexRegister);
        sexNan = (RadioButton)findViewById(R.id.nan);
        sexNv = (RadioButton)findViewById(R.id.nv);
        sexRegister.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("Johnson","===========" + i + sexNan.isChecked() +"    "+ sexNv.isChecked());
            }
        });
        sexNan.setChecked(true);
    }

    public void onRegisterClick(View view) {
//        registerStudent();
        registerTeacher();
    }

    private void registerStudent() {
        username = String.valueOf(userNameEdt.getText());
        pwd = String.valueOf(pwdEdt.getText());
        tel = String.valueOf(telEdt.getText());
        if("".equals(username)){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(pwd)){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
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
        RegisterService registerService = retrofit.create(RegisterService.class);
        Log.d("Johnson",username   + "    "  + pwd)      ;
        Call<String> registCall =registerService.regist(username,pwd,tel);
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
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                Log.d("Johnson","body = " + body );
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(body, UserBean.class);
                Student student = gson.fromJson(body, Student.class);
                Log.d("Johnson",userBean.toString());
                StudentManager.setStudent(student);
                if(userBean.getErrorno() == 1){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, DetailActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                    RegisterActivity.this.startActivity(intent);
                }
                Log.d("Johnson","======" + student.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson","=========onFailure=========2");
            }
        });
    }

    private void registerTeacher() {
        username = String.valueOf(userNameEdt.getText());
        pwd = String.valueOf(pwdEdt.getText());
        tel = String.valueOf(telEdt.getText());
        if("".equals(username)){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(pwd)){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
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
        RegisterService registerService = retrofit.create(RegisterService.class);
        Log.d("Johnson",username   + "    "  + pwd)      ;
        Call<String> registCall =registerService.registTeacher(username,pwd,tel);
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
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                Log.d("Johnson","body = " + body );
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(body, UserBean.class);
                Teacher teacher = gson.fromJson(body, Teacher.class);
                Log.d("Johnson",userBean.toString());
                TeacherManager.setTeacher(teacher);
                if(userBean.getErrorno() == 1){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                    RegisterActivity.this.startActivity(intent);
                }
                Log.d("Johnson","======" + teacher.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson","=========onFailure=========2");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}