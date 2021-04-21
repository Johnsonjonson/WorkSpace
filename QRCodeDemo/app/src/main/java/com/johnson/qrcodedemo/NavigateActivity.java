package com.johnson.qrcodedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.johnson.qrcodedemo.http.NameValuePair;
import com.johnson.qrcodedemo.http.OkHttpUtil;

import java.io.IOException;
import java.util.ArrayList;

public class NavigateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        获取上一个activity传过来的参数
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("data");
        int index =0;
        String chepaiStr = "";
        String yuyueStr = "";
        String phoneStr = "";
        if (bundle!=null) {
            index = bundle.getInt("index");
            chepaiStr = bundle.getString("chepai");
            yuyueStr = "停车位"+index;
            phoneStr = bundle.getString("phone");
        }
        setContentView(R.layout.activity_navigate);
        ImageView imageView = findViewById(R.id.image);
        TextView yuyue = findViewById(R.id.yuyuechewei);
        TextView chepai = findViewById(R.id.chepai);
        TextView phone = findViewById(R.id.phone);
        yuyue.setText("预约车位："+yuyueStr);
        chepai.setText("车牌信息："+chepaiStr);
        phone.setText("联系电话："+phoneStr);
        initImageView(imageView);
        updateImage(index, imageView);

    }

    private void initImageView(ImageView imageView) {
        //计算图片左右间距之和
        int padding = 15;
        int spacePx = (int) (UIUtil.dp2px(this, padding) * 2);
        //计算图片宽度
        int imageWidth = UIUtil.getScreenWidth(this) - spacePx;
        //计算宽高比，注意数字后面要加上f表示浮点型数字
        float scale = 797f / 569f;
        //根据图片宽度和比例计算图片高度
        int imageHeight = (int) (imageWidth / scale);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( imageWidth,imageHeight);
        //设置左右边距
        params.leftMargin = (int) UIUtil.dp2px(this, padding);
        params.rightMargin = (int) UIUtil.dp2px(this, padding);

        imageView.setLayoutParams(params);
    }

    private void updateImage(int index, ImageView imageView) {
        switch (index){
            case 1:
                imageView.setBackgroundResource(R.drawable.park_1);
                break;
            case 2:
                imageView.setBackgroundResource(R.drawable.park_2);
                break;
            case 3:
                imageView.setBackgroundResource(R.drawable.park_3);
                break;
            case 4:
                imageView.setBackgroundResource(R.drawable.park_4);
                break;
            case 5:
                imageView.setBackgroundResource(R.drawable.park_5);
                break;
            case 6:
                imageView.setBackgroundResource(R.drawable.park_6);
                break;
            case 7:
                imageView.setBackgroundResource(R.drawable.park_7);
                break;
            case 8:
                imageView.setBackgroundResource(R.drawable.park_8);
                break;
            case 9:
                imageView.setBackgroundResource(R.drawable.park_9);
                break;
            case 10:
                imageView.setBackgroundResource(R.drawable.park_10);
                break;

        }
    }

    @Override
    public void onBackPressed() {
//        return true
//        super.onBackPressed();
    }

    public void onLeaveClick(View view) {
        updateData();
    }

    public void updateData(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        ArrayList<NameValuePair> param = new ArrayList<>();
                        param.add(new NameValuePair("index","0"));
                        param.add(new NameValuePair("phone",""));
                        param.add(new NameValuePair("chepai",""));
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParams(OkHttpUtil.SET_DATA_URL,param));
                        new Handler(MainActivity.getActivity().getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                startActivity(new Intent(NavigateActivity.this,MainActivity.class));
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(NavigateActivity.this, "请求离开失败，请重试", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            Toast.makeText(NavigateActivity.this, "请求离开失败，请重试", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}