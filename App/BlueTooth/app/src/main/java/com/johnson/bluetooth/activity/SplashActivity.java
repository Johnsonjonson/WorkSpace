package com.johnson.bluetooth.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.johnson.bluetooth.R;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    private ImageView mImgStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        mImgStart = (ImageView) findViewById(R.id.id_img_start);
        iniImage();
    }

    private void iniImage() {
        File dir = getFilesDir();
        File imageFile = new File(dir, "start.jpg");
        if(imageFile.exists()) {
            mImgStart.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        }else {
            mImgStart.setImageResource(R.mipmap.start);
        }

        ScaleAnimation scaleAnim = new ScaleAnimation(
                1.0f,
                1.0f,
                1.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                1.0f
        );

        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(2000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //在这里做一些初始化的操作
                //跳转到指定的Activity
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImgStart.startAnimation(scaleAnim);
    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}