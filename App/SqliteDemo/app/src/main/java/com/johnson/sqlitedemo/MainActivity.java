package com.johnson.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etAge;
    private EditText etSex;
    private EditText etWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etSex = findViewById(R.id.et_sex);
        etWeight = findViewById(R.id.et_weight);
        etSex.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
    }

    public void onConfirmClick(View view) {
        String  name = etName.getText().toString();
        String  sex = etSex.getText().toString();
        String  age = etAge.getText().toString();
        String  weight = etWeight.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(sex)){
            Toast.makeText(this, "性别不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(age)){
            Toast.makeText(this, "年龄不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(weight)){
            Toast.makeText(this, "体重不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = SqliteDB.getInstance(this).saveUser(new User(name,sex,Integer.valueOf(age),Double.valueOf(weight)));
        switch (result) {
            case 0:
                break;
            case 1:
                Toast.makeText(this, "创建用户成功", Toast.LENGTH_SHORT).show();
                break;
            case -1:  //用户已存在
                Toast.makeText(this, "用户已存在，请重新输入，或选择该用户", Toast.LENGTH_SHORT).show();
                break;
            case 2: //创建用户失败
                Toast.makeText(this, "创建用户失败，请重试", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onLoadClick(View view) {
       List<User> users =  SqliteDB.getInstance(this).loadUser();
        Log.d("Johnson",users.toString());

    }
}