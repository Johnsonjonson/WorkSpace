package com.johnson.qrcodedemo;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class QRCodeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ZXingLibrary.initDisplayOpinion(this);
//
    }
}
