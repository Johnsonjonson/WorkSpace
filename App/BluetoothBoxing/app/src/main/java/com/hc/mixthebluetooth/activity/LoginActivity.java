package com.hc.mixthebluetooth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.basiclibrary.titleBasic.DefaultNavigationBar;
import com.hc.basiclibrary.viewBasic.BasActivity;
import com.hc.basiclibrary.viewBasic.tool.IMessageInterface;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.single.HoldBluetooth;
import com.hc.mixthebluetooth.bean.User;
import com.hc.mixthebluetooth.customView.DividerItemDecoration;
import com.hc.mixthebluetooth.recyclerData.UserAdapter;
import com.hc.mixthebluetooth.sqlite.SqliteDB;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BasActivity {

    private EditText mNumberEditText;
    private PopupWindow mPopupWindow;
    private RecyclerView mRecyclerView;
    private UserAdapter mRecyclerViewAdapter;
    private ArrayList<User> mUserList;
    private LinearLayout mselectUserView;
    private TextView mUserName;
    private EditText etName;
    private EditText etAge;
    private EditText etSex;
    private EditText etWeight;
    private AlertDialog registerDialog;
    private User selectedUser;
    private HoldBluetooth mHoldBluetooth;
    private DefaultNavigationBar mTitle;
    private List<DeviceModule> modules;
    private IMessageInterface mMessage;
    private DeviceModule mErrorDisconnect;
    private final String CONNECTED = "已连接",CONNECTING = "连接中",DISCONNECT = "断线了";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setContext(this);
        loadUsers();
        initRecyclerView();

        mNumberEditText = (EditText) findViewById(R.id.number);
        mselectUserView = (LinearLayout) findViewById(R.id.select_user_view);
        mUserName = (TextView) findViewById(R.id.user_name);
    }

    @Override
    public void initAll() {
        mHoldBluetooth = HoldBluetooth.getInstance();
        initDataListener();
        initTitle();
    }

    private void initTitle() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = ((TextView) v).getText().toString();
                if (str.equals(CONNECTED)){
                    if (modules != null && mHoldBluetooth != null) {
                        mHoldBluetooth.tempDisconnect(modules.get(0));
                        setState(DISCONNECT);//设置断线状态
                    }
                }else if (str.equals(DISCONNECT)){
                    if ((modules != null || mErrorDisconnect != null) && mHoldBluetooth != null){
                        mHoldBluetooth.connect(modules!= null&&modules.get(0)!=null?modules.get(0):mErrorDisconnect);
//                        log("开启连接动画..");
                        setState(CONNECTING);//设置正在连接状态
                    }else {
//                        toast("连接失败...",Toast.LENGTH_SHORT);
                        setState(DISCONNECT);//设置断线状态
                    }
                }
            }
        };
        mTitle = new DefaultNavigationBar
                .Builder(this,(ViewGroup)findViewById(R.id.login_layout))
                .setTitle("拳击数据")
                .setRightText(CONNECTING)
                .setRightClickListener(listener)
                .builer();
        mTitle.updateLoadingState(true);
    }

    //初始化蓝牙数据的监听
    private void initDataListener() {
        HoldBluetooth.OnReadDataListener dataListener = new HoldBluetooth.OnReadDataListener() {
            @Override
            public void readData(String mac, byte[] data) {
            }

            @Override
            public void reading(boolean isStart) {
            }

            @Override
            public void connectSucceed() {
            }

            @Override
            public void errorDisconnect(final DeviceModule deviceModule) {//蓝牙异常断开
            }

            @Override
            public void readNumber(int number) {
            }

            @Override
            public void readLog(String className, String data, String lv) {
                //拿到日志
            }
        };
        mHoldBluetooth.setOnReadListener(dataListener);
    }

    private void setState(String state){
        switch (state){
            case CONNECTED://连接成功
                mTitle.updateRight(CONNECTED);
                mErrorDisconnect = null;
                break;

            case CONNECTING://连接中
                mTitle.updateRight(CONNECTING);
                mTitle.updateLoadingState(true);
                break;

            case DISCONNECT://连接断开
                mTitle.updateRight(DISCONNECT);
                break;
        }
    }

    private void loadUsers() {
        mUserList = SqliteDB.getInstance(this).loadUser();
        //初始化数据
        if (mRecyclerViewAdapter!=null) {
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    public void onSelectClick(View view) {
        showSelectNumberPopupWindow();
        Toast.makeText(this, "选择用户被点击", Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出选择号码的对话框
     */
    private void showSelectNumberPopupWindow() {
        mPopupWindow = new PopupWindow(mRecyclerView, mselectUserView.getWidth() - 4, 600);
        mPopupWindow.setOutsideTouchable(true);   // 设置外部可以被点击
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

        mPopupWindow.setFocusable(true);    // 使PopupWindow可以获得焦点
        // 显示在输入框的左下角
        mPopupWindow.showAsDropDown(mselectUserView, 2, -5);
        loadUsers();
        initRecyclerView();
    }

    /**
     * 初始化RecyclerView，模仿ListView下拉列表的效果
     */
    private void initRecyclerView(){
        mRecyclerView = new RecyclerView(this);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setBackgroundResource(R.drawable.button_back);
//        mRecyclerView.setV
        //设置Adapter
        mRecyclerViewAdapter = new UserAdapter(this, mUserList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        //设置点击事件
        mRecyclerViewAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedUser = mUserList.get(position);

                mUserName.setText(selectedUser.getName());
//                mNumberEditText.setSelection(mNumberEditText.getText().toString().length());
                mPopupWindow.dismiss();
            }
        });

        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    public void onBtnCreateClick(View view) {
//        AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 获取布局
        View view2 = View.inflate(this, R.layout.register_layout, null);
        // 获取布局中的控件
        etName = view2.findViewById(R.id.et_name);
        etAge = view2.findViewById(R.id.et_age);
        etSex = view2.findViewById(R.id.et_sex);
        etWeight = view2.findViewById(R.id.et_weight);
        etSex.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        // 设置参数
        builder.setTitle("新建玩家").setIcon(R.drawable.ic_launcher_background).setView(view2);
        // 创建对话框
        registerDialog = builder.show();
        registerDialog.show();
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
        loadUsers();
        initRecyclerView();
        if(null != registerDialog)
            registerDialog.dismiss();
    }

    // 进入
    public void onBtnEnterClick(View view) {
        if(selectedUser==null){
            Toast.makeText(this, "请选择用户", Toast.LENGTH_SHORT).show();
        }else{
            if (!mTitle.getParams().mRightText.equals("已连接")){
                Toast.makeText(this, "未连接蓝牙，请先连接", Toast.LENGTH_SHORT).show();
                return;
            }else {
                startActivity(new Intent(this,CommunicationActivity.class));
            }
        }
    }
}