package com.johnson.tencentmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.johnson.tencentmap.peisong.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://129.204.232.210:8546/";

    private MapView mapView;
    private TencentMap map;

    private LocationService locationService;
    private Timer timer;
    private Marker marker;
    private Marker marker2;
    private Button button;
    private LatLng latlng;
    private LatLng latlng2;
    private Button buttonYuyue;
    private boolean isBooked = false;
    private TextView peisongInfo;
    private TextView tvTiwen;
    private TextView tvPhone;
    private TextView tvDistance;
    private TextView tvTime;
    private TextView tvLocation;
    private TextView tvArriveTime;
    private LinearLayout infoLayout;

    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        locationService = retrofit.create(LocationService.class);
    }

    private void log(String message) {
        Log.i(MainActivity.class.getName(), message);
    }

    private void updateLocation1(Location location) {
        log("updateLocation1: " + location);
        if (marker != null && location.getLat() != 0 && location.getLng() != 0 && location.getLat() >= -90 && location.getLat() <= 90 && location.getLng() >= -180 && location.getLng() <= 180) {
            synchronized (marker) {
                marker.setVisible(true);
                marker2.setVisible(false);
                peisongInfo.setText("配送员1正在配送");
                tvTiwen.setText("体温：36.4°c");
                tvPhone.setText("联系方式：18896358986");
                tvDistance.setText("配送路程：40km");
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String sim = dateFormat.format(date);
                tvTime.setText("当前时间："+sim);
                tvLocation.setText("地点：("+location.getLat()+","+location.getLng()+")");
                tvArriveTime.setText("预计配送时间：20分钟");
                latlng = new LatLng(location.getLat(), location.getLng());
                marker.setPosition(latlng);
                map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            }
        }
    }



    private void updateLocation2(Location location) {
        log("updateLocation2: " + location);
        if (marker2 != null && location.getLat() != 0 && location.getLng() != 0 && location.getLat() >= -90 && location.getLat() <= 90 && location.getLng() >= -180 && location.getLng() <= 180) {
            synchronized (marker2) {
                marker.setVisible(false);
                marker2.setVisible(true);
                peisongInfo.setText("配送员2正在配送");

                tvTiwen.setText("体温：36.4°c");
                tvPhone.setText("联系方式：18776358846");
                tvDistance.setText("配送路程：30km");
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String sim = dateFormat.format(date);
                tvTime.setText("当前时间："+sim);
                tvArriveTime.setText("预计配送时间：30分钟");
                tvLocation.setText("地点：("+location.getLat()+","+location.getLng()+")");
                latlng2 = new LatLng(location.getLat(), location.getLng());
                marker2.setPosition(latlng2);
                map.moveCamera(CameraUpdateFactory.newLatLng(latlng2));
            }
        }
    }

    public static boolean isInstalled() {
        return new File("/data/data/com.tencent.map").exists();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initMap();
        initMarkers();

    }

    private void initView() {
        mapView = this.findViewById(R.id.mapview);
        peisongInfo = this.findViewById(R.id.peisongInfo); //配送人
        infoLayout = this.findViewById(R.id.infoLayout); //配送信息
        tvTiwen = this.findViewById(R.id.tvTiwen); //配送人 体温
        tvPhone = this.findViewById(R.id.tvPhone); //配送人 联系方式
        tvDistance = this.findViewById(R.id.tvDistance); //配送人 距离
        tvTime = this.findViewById(R.id.tvTime); //配送人 时间
        tvLocation = this.findViewById(R.id.tvLocation); //配送人 地点
        tvArriveTime = this.findViewById(R.id.tvArriveTime); //配送人 预计送达时间
        infoLayout.setVisibility(View.INVISIBLE);
    }

    private void initMap() {
        map = mapView.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setIndoorLevelPickerEnabled(true);
        map.setMapStyle(TencentMap.MAP_TYPE_NONE);
    }

    private void initMarkers() {
        final LatLng latLng = new LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions(latLng);
        marker = map.addMarker(markerOptions);
        marker.setTitle("配送员1");
        marker.showInfoWindow();
        marker.setVisible(false);

        final LatLng latLng1 = new LatLng(0, 0);
        MarkerOptions markerOptions1 = new MarkerOptions(latLng1);
        marker2 = map.addMarker(markerOptions1);
        marker2.setTitle("配送员2");
        marker2.showInfoWindow();
        marker2.setVisible(false);
        peisongInfo.setText("无配送信息");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log("request location");
                try {
                    Call<Data> multiLocation = locationService.getMultiLocation();
                    final Response<Data> locationResponse = multiLocation.execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Data data = locationResponse.body();
                                int id = data.id;
                                if (id == 1) {
                                    updateLocation1(data.location1);
                                    infoLayout.setVisibility(View.VISIBLE);
                                }else if (id ==2){
                                    updateLocation2(data.location2);
                                    infoLayout.setVisibility(View.VISIBLE);
                                }else{
                                    marker.setVisible(false);
                                    marker2.setVisible(false);
                                    peisongInfo.setVisibility(View.VISIBLE);
                                    peisongInfo.setText("无配送信息");
                                    infoLayout.setVisibility(View.INVISIBLE);
                                }
                                Log.d("Johnson"," id = "+ id + "lat1 = " + data.location1.getLat() + " lng1 = "+data.location1.getLng() + " lat2 = "+data.location2.getLat() + " lng2 = "+data.location2.getLng());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    MainActivity.this.log("It has error to request location:" + e.getMessage());
                }
            }
        }, 1000, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        timer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapView.onRestart();
    }
}
