package com.johnson.packingbook.activity;

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
import com.johnson.packingbook.R;
import com.johnson.packingbook.bean.UserBean;
import com.johnson.packingbook.manager.UserManager;
import com.johnson.packingbook.service.RegisterService;

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
        userNameEdt = findViewById(R.id.usernameRegister);
        pwdEdt = findViewById(R.id.passwordRegister);
        telEdt = findViewById(R.id.telRegister);
        sexRegister = findViewById(R.id.sexRegister);
        sexNan = findViewById(R.id.nan);
        sexNv = findViewById(R.id.nv);
        sexRegister.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("Johnson","===========" + i + sexNan.isChecked() +"    "+ sexNv.isChecked());
            }
        });
        sexNan.setChecked(true);
    }

    public void onRegisterClick(View view) {
        registerUser();
//        registerTeacher();
    }

    private void registerUser() {
        username = String.valueOf(userNameEdt.getText());
        pwd = String.valueOf(pwdEdt.getText());
//        tel = String.valueOf(telEdt.getText());
        if("".equals(username)){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(pwd)){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
//        if("".equals(tel)){
//            Toast.makeText(this, "电话不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if(sexNan.isChecked()){
            sex = "male";
        }else if (sexNv.isChecked()){
            sex = "female";
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8535/").build();
        RegisterService registerService = retrofit.create(RegisterService.class);
        Log.d("Johnson",username   + "    "  + pwd)      ;
//        Call<String> registCall =registerService.regist(username,pwd,tel);
        Call<String> registCall =registerService.regist(username,pwd);
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
                Log.d("Johnson","body = " + body );
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(body, UserBean.class);
                Log.d("Johnson",userBean.toString());
                UserManager.setUser(userBean);
                if(userBean.getErrorno() == 1){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, ParkDetailActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                    RegisterActivity.this.startActivity(intent);
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
        super.onBackPressed();
    }
}