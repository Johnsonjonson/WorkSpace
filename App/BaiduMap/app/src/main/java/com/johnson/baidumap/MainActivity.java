package com.johnson.baidumap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Johnson";
    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        // 声明LocationClient类
//        mLocationClient = new LocationClient(getApplicationContext());
//        mBDLocationListener = new MyBDLocationListener();
//        // 注册监听
//        mLocationClient.registerLocationListener(mBDLocationListener);
//        getLocation();
    }

    public void onYuyueClick(View view) {
        BaiduMapNavigation.setSupportWebNavi(false);
        double endLon= 117.162582;
        double endLat= 36.690705;

//        double startLon= longitude;
//        double startLat= latitude;

        double startLon= 116.35885;
        double startLat= 39.914714;
        NaviParaOption para = new NaviParaOption();
        para.startPoint(new LatLng(startLat, startLon));
        para.startName("从这里开始");
        para.endPoint(new LatLng(endLat, endLon));
        para.endName("到这里结束");
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, MainActivity.this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "您尚未安装百度地图app", Toast.LENGTH_SHORT).show();
        }

//        startNavi();

    }

    /** 获得所在位置经纬度及详细地址 */
    public void getLocation() {
        // 声明定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();

    }

    private class MyBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 非空判断
            if (location != null) {
                // 根据BDLocation 对象获得经纬度以及详细地址信息
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                String address = location.getAddrStr();
                Log.i(TAG, "address:" + address + " latitude:" + latitude
                        + " longitude:" + longitude + "—");
                if (mLocationClient.isStarted()) {
                    // 获得位置之后停止定位
                    mLocationClient.stop();
                }
            }
        }
    }

    /**
     * 开始导航
     *
     */
    public void startNavi() {

        double endLon= 117.162582;
        double endLat= 36.690705;

//        double startLon= longitude;
//        double startLat= latitude;

        double startLon= 116.35885;
        double startLat= 39.914714;
        //起点经纬坐标
        LatLng pt1 = new LatLng(startLat, startLon);
        //终点经纬坐标
        LatLng pt2 = new LatLng(endLat, endLon);
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption();
        para.startPoint(pt1);
        para.startName("从这里开始");
        para.endPoint(pt2);
        para.endName ("到这里结束");
        if(isAvilible(getApplicationContext(), "com.baidu.BaiduMap")){
            try {
                Intent intent;
                try {
                    intent = Intent.getIntent("intent://map/direction?origin=latlng:"+startLat+","+startLon+"|name:从这里开始&destination="
                            +"latlng:"+endLat+","+ endLon+"|name:到这里结束"+"&mode=driving®ion=南京&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                    startActivity(intent); //启动调用
                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
                builder.setTitle("提示");
                builder.create().show();
            }
        }else{
            //网页应用调起导航
            BaiduMapNavigation.openWebBaiduMapNavi(para, this);
        }
    }
    /**
     * @function:判断手机是否安装了某应用
     * @param context
     * @param packageName
     * @return
     */
    private boolean isAvilible(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if(pinfo != null){
            for(int i = 0; i < pinfo.size(); i++){
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // 取消监听函数
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
        }
    }
}