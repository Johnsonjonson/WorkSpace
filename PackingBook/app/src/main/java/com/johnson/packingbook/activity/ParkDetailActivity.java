package com.johnson.packingbook.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.johnson.packingbook.R;
import com.johnson.packingbook.bean.Park;
import com.johnson.packingbook.bean.UserBean;
import com.johnson.packingbook.manager.UserManager;
import com.johnson.packingbook.service.APIService;
import com.johnson.packingbook.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ParkDetailActivity extends AppCompatActivity {

    private static final String TAG = "Johnson";
    private ArrayList<Park> parkList = new ArrayList<Park>();
    private TextView tvYuyue;
    private TextView tvKongxian;
    private TextView tvZhanyong;
    private LinearLayout parkLayout1;
    private LinearLayout parkLayout2;
    private LinearLayout parkLayout3;
    private ToggleButton parkToggle1;
    private ToggleButton parkToggle2;
    private ToggleButton parkToggle3;

    private TextView tvPark;
    private ImageButton btnPack;
    private FrameLayout framePack;
    private Park selectedPark;
    private int kongxian = 0;
    private int yuyue = 0;
    private int zhanyong = 0;
    private TextView yuyueStatus;
    private TextView parkName;
    private TextView remainTime;
    private Button btnConfirm;
    private AlertDialog detailDialog;
    private Timer timer;
    {
        timer = new Timer(true);
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
            requestAllPark();
            getUserInfo();
            updateDetailDialog();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_detail);
        initView();
        requestAllPark();
        getUserInfo();
        updateUserStatus();
        initDetailDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_modify:
                final Dialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // 获取布局
                View view = View.inflate(this, R.layout.modify_pwd, null);
                // 获取布局中的控件
                final EditText newPwd = view.findViewById(R.id.new_pwd);
                final EditText oldPwd = view.findViewById(R.id.old_pwd);
                final Button btnModify = view.findViewById(R.id.btn_modify);
                // 设置参数
                builder.setView(view);
                // 创建对话框
                dialog = builder.show();
                btnModify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newPwdStr = String.valueOf(newPwd.getText());
                        String oldPwdStr = String.valueOf(oldPwd.getText());
                        String uerPwd = UserManager.getUser().getPwd();
                        int userid = UserManager.getUser().getId();
                        if("".equals(oldPwdStr)){
                            Toast.makeText(ParkDetailActivity.this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if("".equals(newPwdStr)){
                            Toast.makeText(ParkDetailActivity.this, "请输入新的密码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!uerPwd.equals(oldPwdStr)){
                            Toast.makeText(ParkDetailActivity.this, "密码不正确，请输入正确的密码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(newPwdStr.equals(oldPwdStr)){
                            Toast.makeText(ParkDetailActivity.this, "请不要使用相同的密码", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .baseUrl("http://129.204.232.210:8535/").build();
                        APIService apiService = retrofit.create(APIService.class);
                        Log.d("Johnson",newPwdStr   + "    "  + userid)      ;
                        Call<String> stringCall =apiService.modifyPwd(userid,uerPwd,newPwdStr);
                        stringCall.enqueue(new Callback<String>() {
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
                                if(userBean.getErrorno() == 1){
                                    UserManager.setUser(userBean);
                                    dialog.dismiss();
                                    Toast.makeText(ParkDetailActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ParkDetailActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("Johnson","=========onFailure=========2");
                                Toast.makeText(ParkDetailActivity.this, "修改密码失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }

        return true;

    }

    private void initView() {
        tvYuyue = findViewById(R.id.tv_yuyue);
        tvKongxian = findViewById(R.id.tv_kongxian);
        tvZhanyong = findViewById(R.id.tv_zhanyong);
        parkLayout1 = findViewById(R.id.park_layout_1);
        parkLayout2 = findViewById(R.id.park_layout_2);
        parkLayout3 = findViewById(R.id.park_layout_3);
        parkToggle1 = findViewById(R.id.park_toggle1);
        parkToggle2 = findViewById(R.id.park_toggle2);
        parkToggle3 = findViewById(R.id.park_toggle3);
        btnPack = findViewById(R.id.btn_park);
        framePack = findViewById(R.id.frame_park);
        tvPark = findViewById(R.id.tv_park);
        parkLayout1.setBackground(getDrawable(R.drawable.layout_bg_normal));
        parkLayout2.setBackground(getDrawable(R.drawable.layout_bg_normal));
        parkLayout3.setBackground(getDrawable(R.drawable.layout_bg_normal));
        tvPark.setText("预约");
        btnPack.setClickable(false);
        parkToggle1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "=================1111111" + isChecked);
                if (isChecked) {
                    parkToggle2.setChecked(false);
                    parkToggle3.setChecked(false);
                    updateParkingStatus();
                    parkLayout1.setBackground(getDrawable(R.drawable.layout_bg));
                } else {
                    parkLayout1.setBackground(getDrawable(R.drawable.layout_bg_normal));
                }
            }
        });

        parkToggle2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    parkToggle1.setChecked(false);
                    parkToggle3.setChecked(false);
                    updateParkingStatus();
                    parkLayout2.setBackground(getDrawable(R.drawable.layout_bg));
                } else {
                    parkLayout2.setBackground(getDrawable(R.drawable.layout_bg_normal));
                }
            }
        });

        parkToggle3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    parkToggle2.setChecked(false);
                    parkToggle1.setChecked(false);
                    updateParkingStatus();
                    parkLayout3.setBackground(getDrawable(R.drawable.layout_bg));
                } else {
                    parkLayout3.setBackground(getDrawable(R.drawable.layout_bg_normal));
                }
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void updateParkingStatus() {
        UserBean user = UserManager.getUser();
        Park park = null;
        if (parkToggle1.isChecked()) {
            park = parkList.get(0);
        } else if (parkToggle2.isChecked()) {
            park = parkList.get(1);
        } else if (parkToggle3.isChecked()) {
            park = parkList.get(2);
        }
        int userStatus = 0;
        if (user != null){
            userStatus = user.getStatus();
        }
        if (park != null) {
            selectedPark = park;
            int status = park.getStatus();
            if (status == 0) {
//                tvPark.setText("预约");
//                btnPack.setClickable(true);
                if (userStatus == 0){
                    btnPack.setBackground(getApplication().getDrawable(R.drawable.kongxian_bg));
                    framePack.setBackground(getApplication().getDrawable(R.drawable.kongxian_bg));
                }
            } else if (status == 1) {
//                tvPark.setText("预约中");
//                btnPack.setClickable(false);
                if (userStatus == 0) {
                    Toast.makeText(this, "该车位已被预约，请选择其他车位", Toast.LENGTH_SHORT).show();
                }
            } else if (status == 2) {
//                tvPark.setText("已占用");
//                btnPack.setClickable(false);
                if (userStatus == 0) {
                    Toast.makeText(this, "该车位已被占用，请选择其他车位", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void requestAllPark() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8535/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> loginCall = apiService.getAllPark();
        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String body1 = response.body();
                    body = body1;
                    JSONArray jsonArray = new JSONArray(body);
                    parkList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // JSON数组里面的具体-JSON对象
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int status = jsonObject.optInt("status", 0);
                        int id = jsonObject.optInt("id", 0);
                        long time = jsonObject.optLong("time", 0);
                        int userid = jsonObject.optInt("userId", 0);
                        String name = jsonObject.optString("name", "");
                        parkList.add(new Park(status, id, time, userid, name));
                        // 日志打印结果：
                        Log.d(TAG, "analyzeJSONArray1 解析的结果：status" + status + " id:" + id);
                    }
                    updatePark();
                    updateUserStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson", "=========onFailure=========2");
                Toast.makeText(ParkDetailActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserInfo() {
        UserBean user = UserManager.getUser();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8535/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> loginCall = apiService.queryUser(user.getId());
        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String body1 = response.body();
                    body = body1;
                    Log.d("Johnson", "body = " + body);
                    Gson gson = new Gson();
                    UserBean userBean = gson.fromJson(body, UserBean.class);
                    Log.d("Johnson", userBean.toString());
                    UserBean user = UserManager.getUser();
                    if (user.getStatus()==1 && userBean.getStatus()==2){ //预约变已停车
                        Toast.makeText(ParkDetailActivity.this, "停车成功", Toast.LENGTH_SHORT).show();
                    }
                    UserManager.setUser(userBean);
                    updateUserStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson", "=========onFailure=========2");
                Toast.makeText(ParkDetailActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserStatus() {
        UserBean user = UserManager.getUser();
        if (user != null) {
            int status = user.getStatus();
            if (status == 1) {  // 预约中
                tvPark.setText("预约中");
                btnPack.setClickable(false);
                btnPack.setBackground(getApplication().getDrawable(R.drawable.yuyue_bg));
                framePack.setBackground(getApplication().getDrawable(R.drawable.yuyue_bg));
            } else if (status == 2) {//已停车
                tvPark.setText("取车");
                btnPack.setClickable(true);
                btnPack.setBackground(getApplication().getDrawable(R.drawable.kongxian_bg));
                framePack.setBackground(getApplication().getDrawable(R.drawable.kongxian_bg));
            } else if (status == 0) {//未预约
                tvPark.setText("预约");
                if (kongxian > 0) {
                    btnPack.setClickable(true);
                    btnPack.setBackground(getApplication().getDrawable(R.drawable.kongxian_bg));
                    framePack.setBackground(getApplication().getDrawable(R.drawable.kongxian_bg));
                } else {
                    btnPack.setClickable(false);
                    btnPack.setBackground(getApplication().getDrawable(R.drawable.zhanyong_bg));
                    framePack.setBackground(getApplication().getDrawable(R.drawable.zhanyong_bg));
                }
            }else if (status == 3) {//已预约  预约超时未停车
                tvPark.setText("预约超时");
                btnPack.setClickable(true);
                btnPack.setBackground(getApplication().getDrawable(R.drawable.zhanyong_bg));
                framePack.setBackground(getApplication().getDrawable(R.drawable.zhanyong_bg));
            }
        }
    }

    private void updatePark() {
        kongxian = 0;
        yuyue = 0;
        zhanyong = 0;
        if (parkList.size() > 0) {
            for (Object o : parkList) {
                Park park = (Park) o;
                int status = park.getStatus();
                if (status == 0) { // 空闲
                    kongxian++;
                } else if (status == 1) {//预约中
                    yuyue++;
                } else if (status == 2) {// 占用中
                    zhanyong++;
                }
                if (park != null) {
                    switch (park.getId()) {
                        case 1:
                            updateParkToggle(parkToggle1, status);
                            break;
                        case 2:
                            updateParkToggle(parkToggle2, status);
                            break;
                        case 3:
                            updateParkToggle(parkToggle3, status);
                            break;
                    }
                }
            }

            tvKongxian.setText("空闲：" + kongxian);
            tvYuyue.setText("预约中：" + yuyue);
            tvZhanyong.setText("已占用：" + zhanyong);
        }
    }

    private void updateParkToggle(ToggleButton toggleButton, int status) {
        if (status == 0) {
            toggleButton.setBackground(getApplication().getDrawable(R.drawable.park_kongxian_02e4f1));
        } else if (status == 1) {
            toggleButton.setBackground(getApplication().getDrawable(R.drawable.park_yuyue_e0620d));
        } else if (status == 2) {// 占用中
            toggleButton.setBackground(getApplication().getDrawable(R.drawable.park_zhanyong_cdcdcd));
        }
    }

    private Park choosePark() {
        if (parkList.size() > 0) {
            for (Park o : parkList) {
                Park park = o;
                int status = park.getStatus();
                if (status == 0) { // 空闲
                    return park;
                }
            }
        }

        return null;
    }


    public void onParkClick(View view) {
        UserBean user = UserManager.getUser();
        if (user != null) {
            int userStatus = user.getStatus();
            if (userStatus == 0) { // 未预约
                //预约
                if (kongxian > 0) {
                    //预约
                    if (selectedPark == null) {
                        selectedPark = choosePark();
                    }
                    if (selectedPark != null) {
                        int status = selectedPark.getStatus();
                        int packId = selectedPark.getId();
                        if (status == 0) {
                            bookPark(user.getId(), selectedPark.getId());
                            Log.d(TAG, "预约点击   ID=  " + packId);
                        } else if (status == 1)
                            Log.d(TAG, "预约中点击   ID=  " + packId);
                        else
                            Log.d(TAG, "已占用点击   ID=  " + packId);
                    }

                } else {
                    //停车位已满  不能停车
                    Toast.makeText(this, "停车位已满,不能预约停车", Toast.LENGTH_SHORT).show();
                }
            } else if (userStatus == 2) { // 已停车
                //取车
                Log.d("Johnson",user.getId()+ "    " +user.getParkId());
                takeCar(user.getId(), user.getParkId());
            } else if(userStatus ==3){ //预约但未及时停车
                Toast.makeText(this, "由于您上次预约未及时停车，将暂停预约服务2小时", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void takeCar(int id, int packID) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8535/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> takeCarCall = apiService.takeCar(id, packID);
        takeCarCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String body1 = response.body();
                    body = body1;
                    Log.d("Johnson", "取车 body = " + body);
                    JSONObject jsParam = new JSONObject(body);
                    int errorno = jsParam.optInt("errorno", 0);
                    if (errorno == 0) {
                        Toast.makeText(ParkDetailActivity.this, "取车失败", Toast.LENGTH_SHORT).show();
                        //取车失败
                    } else if (errorno == 1) {
                        //取车成功
                        Toast.makeText(ParkDetailActivity.this, "取车成功", Toast.LENGTH_SHORT).show();
                    } else if (errorno == 2) {
                        //车还在车库 暂时不能取车
                        Toast.makeText(ParkDetailActivity.this, "取车失败，暂时不能取车", Toast.LENGTH_SHORT).show();
                    }
                    // 更新信息
                    getUserInfo();
                    requestAllPark();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson", "===取车======onFailure=========2");
                Toast.makeText(ParkDetailActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bookPark(int userid, int packID) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8535/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> bookParkCall = apiService.bookPark(userid, packID);
        bookParkCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String body1 = response.body();
                    body = body1;

                    JSONObject jsParam = new JSONObject(body);
                    int errorno = jsParam.optInt("errorno", 0);
                    String bookTime = jsParam.optString("bookTime", "");
                    String bookTimeStamp = DateUtil.date2TimeStamp(bookTime, "yyyy-MM-dd HH:mm:ss");
                    Log.d("Johnson", "预约 车位 body = " + body + " bookTime = " + bookTime + " bookTimeStamp = " + bookTimeStamp);
                    if (errorno == 0) {
                        //预约失败
                        Toast.makeText(ParkDetailActivity.this, "预约失败", Toast.LENGTH_SHORT).show();
                    } else if (errorno == 1) {
                        //预约成功
                        Toast.makeText(ParkDetailActivity.this, "预约成功，请在1小时内将车停入车库", Toast.LENGTH_SHORT).show();
                    } else if (errorno == 2) {
                        //车还在车库 暂时不能取车
                    }
                    // 更新信息
                    getUserInfo();
                    requestAllPark();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Johnson", "=======预约 车位==onFailure=========2");
                Toast.makeText(ParkDetailActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onDetalClick(View view) {
        updateDetailDialog();
        detailDialog.show();
    }

    private void updateDetailDialog() {
        UserBean user = UserManager.getUser();
        if (user!=null) {
            int status = user.getStatus();
            int parkId = user.getParkId();
            String bookTime = user.getBookTime();
            String parkTime = user.getParkTime();
            String curTimeStamp = DateUtil.timeStamp();
            long bookTimeLong =0;
            if(bookTime.equals("None"))
                bookTimeLong =0;
            else {
                String bookTimeStamp = DateUtil.date2TimeStamp(bookTime, "yyyy-MM-dd HH:mm:ss");
                bookTimeLong = Long.valueOf(bookTimeStamp);
            }
            long parkTimeLong =0;
            if(parkTime.equals("None"))
                parkTimeLong =0;
            else {
                String parkTimeStamp = DateUtil.date2TimeStamp(parkTime, "yyyy-MM-dd HH:mm:ss");
                parkTimeLong = Long.valueOf(parkTimeStamp);
            }
            long curTimeLong = Long.valueOf(curTimeStamp);
            long difTime = 1*60*60;
            long outTime = 1*60*60;
            long remainTimeLong = difTime - (curTimeLong- bookTimeLong);
            long remainTimePark = outTime - (curTimeLong- bookTimeLong);
            long parkedTime = curTimeLong- parkTimeLong;
            String remainTimeStr = DateUtil.second2HMS(remainTimeLong);
            String parkedTimeStr = DateUtil.second2HMS(parkedTime);
            if(status ==0){ // 空闲
                yuyueStatus.setText("状态：未预约");
                parkName.setVisibility(View.GONE);
                if (remainTimePark > 0 || remainTimePark<-2*60*60)
                    remainTime.setVisibility(View.GONE);
                else{
                    remainTime.setText("预约后超2小时未停车，未来两小时将无法进行预约");
                    remainTime.setVisibility(View.VISIBLE);
                }
            }else if(status ==1){ // 已预约
                yuyueStatus.setText("状态：已预约");
                parkName.setText("预约停车位：停车位"+parkId);
                remainTime.setText("预约剩余时间: "+remainTimeStr);
                remainTime.setVisibility(View.VISIBLE);
                parkName.setVisibility(View.VISIBLE);
            }else if (status ==2){//已停车
                yuyueStatus.setText("状态：已停车");
                parkName.setVisibility(View.VISIBLE);
                remainTime.setVisibility(View.VISIBLE);
                remainTime.setText("已停车时间:"+parkedTimeStr);
                parkName.setText("停车车位：停车位"+parkId);
            }else if(status == 3){
                yuyueStatus.setText("状态：未预约");
                parkName.setVisibility(View.GONE);
                remainTime.setText("预约后超1小时未停车，未来两小时将无法进行预约");
                remainTime.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initDetailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 获取布局
        View view2 = View.inflate(this, R.layout.park_detail_layout, null);
        // 获取布局中的控件
        yuyueStatus = view2.findViewById(R.id.tv_yuyue_status);
        parkName = view2.findViewById(R.id.tv_park_name);
        remainTime = view2.findViewById(R.id.yuyue_remain_time);
        btnConfirm = view2.findViewById(R.id.btn_confirm);
        // 设置参数
        builder.setTitle("停车详情").setIcon(R.drawable.ic_launcher_background).setView(view2);
        // 创建对话框
        detailDialog = builder.show();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                detailDialog.dismiss();
            }
        });
        detailDialog.dismiss();
    }
}