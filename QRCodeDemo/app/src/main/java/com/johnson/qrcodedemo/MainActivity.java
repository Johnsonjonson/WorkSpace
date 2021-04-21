package com.johnson.qrcodedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.johnson.qrcodedemo.http.NameValuePair;
import com.johnson.qrcodedemo.http.OkHttpUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1000 ;
    private static final String pass = "4dd0050d8dfb7b1eb9f4d23dd85ec400";

    public static Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        PermissionUtil.checkCameraPermissions(1);
        getData();
    }

    public void onQRScanClick(View view) {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    String[] strings = result.split(",");
                    String strdata = result;
                    if(strings.length>0){
                        strdata = strings[0];
                    }
                    if (pass.equals(strdata)){
//                        Toast.makeText(this, "匹配成功" + strdata, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this,ParkDetailActivity.class));
                    }
//                    Toast.makeText(this, "解析结果:" + strdata, Toast.LENGTH_LONG).show();

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void getData(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.BASE_URL);
                        new Handler(MainActivity.getActivity().getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                JSONObject jsParam = null;
                                try {
                                    jsParam = new JSONObject(result);
                                    int selectedIndex = jsParam.optInt("index", 0);
                                    String chepai = jsParam.optString("chepai", "");
                                    String phone = jsParam.optString("phone", "");
                                    if(selectedIndex >0){
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("index", selectedIndex);
                                        bundle.putString("chepai", chepai);
                                        bundle.putString("phone", phone);
                                        Intent intent = new Intent(MainActivity.this, NavigateActivity.class);
                                        intent.putExtra("data", bundle);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Activity getActivity() {
        return mActivity;
    }
}