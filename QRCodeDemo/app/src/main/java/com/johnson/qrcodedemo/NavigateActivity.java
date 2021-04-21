package com.johnson.qrcodedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NavigateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        获取上一个activity传过来的参数
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("data");
        int index =0;
        if (bundle!=null)
            index = bundle.getInt("index");
        setContentView(R.layout.activity_navigate);
        ImageView imageView = findViewById(R.id.image);
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

    public void onLeaveClick(View view) {
    }
}