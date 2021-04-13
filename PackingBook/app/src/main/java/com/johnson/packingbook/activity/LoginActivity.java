package com.johnson.packingbook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.johnson.packingbook.R;
import com.johnson.packingbook.manager.StudentManager;
import com.johnson.packingbook.bean.UserBean;
import com.johnson.packingbook.manager.UserManager;
import com.johnson.packingbook.service.LoginService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 登陆
 */
public class LoginActivity extends AppCompatActivity {

    private String username;
    private String pwd;
    private EditText userNameEdt;
    private EditText pwdEdt;
    private int backPressedNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameEdt = findViewById(R.id.username);
        pwdEdt = findViewById(R.id.password);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        StudentManager.setStudent(null);
    }

    private void requestLogin(){
        username = String.valueOf(userNameEdt.getText());
        pwd = String.valueOf(pwdEdt.getText());
        if("".equals(username)){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if("".equals(pwd)){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8535/").build();
        LoginService loginService = retrofit.create(LoginService.class);
        Log.d("Johnson",username   + "    "  + pwd)      ;
        Call<String> loginCall =loginService.login(username,pwd);
        loginCall.enqueue(new Callback<String>() {
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
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, ParkDetailActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                    LoginActivity.this.startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson","=========onFailure=========2");
                Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void onLoginClick(View view) {
        requestLogin(); // 用户登陆
    }

    public void onLoginRegisterClick(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        backPressedNum ++;
        if (backPressedNum ==1){
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
        }else{
            backPressedNum =0;
            killAppProcess();
        }
    }
    public void killAppProcess()
    {
        //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
        ActivityManager mActivityManager = (ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList)
        {
            if (runningAppProcessInfo.pid != android.os.Process.myPid())
            {
                android.os.Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}