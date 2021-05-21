package com.johnson.qrcodedemo;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {
    /**
     * 申请权限回调使用常量
     */
    public final static int PERMISSIONS_REQUEST_READ_PHONE_STATE = 1001;
    public final static int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1002;
    public final static int PERMISSIONS_REQUEST_CAMERA = 1003;
    public final static int PERMISSIONS_REQUEST_RECORD_AUDIO = 1004;
    public final static int PERMISSIONS_REQUEST_READ_CONTACTS = 1005;
    public final static int PERMISSIONS_REQUEST_SEND_SMS = 1006;
    public final static int PERMISSIONS_REQUEST_CALL_PHONE_STATE = 1007;
    public final static int PERMISSIONS_REQUEST_LOCATION = 1008;
    public final static int PERMISSIONS_REQUEST_CAMERA_QRCODE = 1009;//扫描二维码请求相机权限
    public final static int PERMISSIONS_REQUEST_CAMERA_SAVEIMG = 1010;//获取头像等请求相机权限


    public static boolean isPermissionChecking = false;
    public static boolean isPermissionDialogShow = false;

    public static boolean checkStoPermission(String tag){
        if(ContextCompat.checkSelfPermission(MainActivity.mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(!isPermissionChecking){
                ActivityCompat.requestPermissions(
                        MainActivity.mActivity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                isPermissionChecking = true;
            }
            return false;
        }
        return true;
    }

    public static boolean checkPhonePermissions(String tag){
//        Log.d("janroid","*******************************"+tag);

        if(ContextCompat.checkSelfPermission(MainActivity.mActivity, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainActivity.mActivity,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
            return false;
        }

        return true;
    }

    public static boolean checkCallPhonePermissions(){
        if(ContextCompat.checkSelfPermission(MainActivity.mActivity, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainActivity.mActivity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_REQUEST_CALL_PHONE_STATE);
            return false;
        }
        return true;
    }

    public static boolean checkCameraPermissions(int permissionReqCode){
        if(ContextCompat.checkSelfPermission(MainActivity.mActivity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainActivity.mActivity,
                    new String[]{Manifest.permission.CAMERA},
                    permissionReqCode);
            return false;
        }

        return true;
    }

    public static boolean checkAudioPermissions(String tag){
        if(ContextCompat.checkSelfPermission(MainActivity.mActivity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainActivity.mActivity,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_REQUEST_RECORD_AUDIO);
            return false;
        }

        return true;
    }

    public static boolean checkContactsPermissions(String tag){
        if(ContextCompat.checkSelfPermission(MainActivity.mActivity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainActivity.mActivity,
                    new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
        }

        return true;
    }

    public static boolean checkSmsPermissions(String tag){
        if(ContextCompat.checkSelfPermission(MainActivity.mActivity, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    MainActivity.mActivity,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
            return false;
        }

        return true;
    }
}
