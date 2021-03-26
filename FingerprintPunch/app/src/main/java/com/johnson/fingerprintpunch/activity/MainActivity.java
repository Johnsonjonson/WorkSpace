package com.johnson.fingerprintpunch.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.johnson.fingerprintpunch.R;
import com.johnson.fingerprintpunch.adapter.StudentAdapter;
import com.johnson.fingerprintpunch.bean.Teacher;
import com.johnson.fingerprintpunch.manager.StudentManager;
import com.johnson.fingerprintpunch.bean.Student;
import com.johnson.fingerprintpunch.manager.TeacherManager;
import com.johnson.fingerprintpunch.service.APIService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.os.Build.VERSION.SDK_INT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Johnson";
    private ListView listView;
    private TextView tvTeacher;
    private int backPressedNum = 0;

    private List<Student> studentList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        listView = findViewById(R.id.lv_student);
        tvTeacher = findViewById(R.id.tv_teacher);
        // 在这个方法中可以通过position参数判断出用户点击的是那一个子项
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student=studentList.get(position);
                StudentManager.setStudent(student);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);
            }
        });
        getAllStudentData();
        updateData();
    }

    private void updateData() {
        Teacher teacher = TeacherManager.getTeacher();
        if (teacher!=null)
            tvTeacher.setText("您好！"+teacher.getName()+"老师");
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllStudentData();
    }

    private void init() {
        if (SDK_INT < Build.VERSION_CODES.P) {
            return;
        }
        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
            Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
            Object sVmRuntime = getRuntime.invoke(null);
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{new String[]{"L"}});
        } catch (Throwable e) {
            Log.e("[error]", "reflect bootstrap failed:", e);
        }
    }

    private void getAllStudentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8533/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> allUserCall =apiService.getAllUser();
        allUserCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                studentList.clear();
                try {
                    String body1 = response.body();
                    body = body1;
                    JSONArray jsonArray = new JSONArray(body);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // JSON数组里面的具体-JSON对象
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Gson gson = new Gson();
                        Student student = gson.fromJson(jsonObject.toString(), Student.class);
                        studentList.add(student);
                        // 日志打印结果：
                        Log.d(TAG, student.toString());
                    }
                    StudentAdapter adapter=new StudentAdapter(MainActivity.this,R.layout.student_item,studentList);
                    listView.setAdapter(adapter);
                } catch (Exception e) {
                    listView.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRegisterClick(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onLoginClick(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(this,LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onInputStudentClick(View view) {
        Intent intent = new Intent(this, AddStudentActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onRefreshClick(View view) {
        getAllStudentData();
    }
}