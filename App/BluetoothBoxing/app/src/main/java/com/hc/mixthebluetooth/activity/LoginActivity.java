package com.hc.mixthebluetooth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.bean.User;
import com.hc.mixthebluetooth.customView.DividerItemDecoration;
import com.hc.mixthebluetooth.recyclerData.UserAdapter;
import com.hc.mixthebluetooth.sqlite.SqliteDB;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

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
    private AlertDialog queryDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadUsers();
        initRecyclerView();
        mNumberEditText = (EditText) findViewById(R.id.number);
        mselectUserView = (LinearLayout) findViewById(R.id.select_user_view);
        mUserName = (TextView) findViewById(R.id.user_name);
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
                User user = mUserList.get(position);
                mUserName.setText(user.getName());
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
        queryDialog = builder.show();
        queryDialog.show();
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
    }
}