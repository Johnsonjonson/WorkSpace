package com.johnson.tencentmap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://129.204.232.210:8544/";

    private MapView mapView;
    private TencentMap map;
    private int index;
    private LocationService locationService;
    private Timer timer;
    private Marker marker;
    private Button button;
    private LatLng latlng;
    private Button buttonPark;
    private boolean isBooked = true;

    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        locationService = retrofit.create(LocationService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        index = 0;
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("data");
        if (bundle!=null) {
            index = bundle.getInt("index");
        }
        mapView = this.findViewById(R.id.mapview);
        button = this.findViewById(R.id.button);
        buttonPark = this.findViewById(R.id.button1);
        map = mapView.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setIndoorLevelPickerEnabled(true);
        map.setMapStyle(TencentMap.MAP_TYPE_NONE);
        final LatLng latLng = new LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions(latLng);
        marker = map.addMarker(markerOptions);
        marker.setTitle("车位");
        marker.showInfoWindow();
        marker.setVisible(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String uriString = "qqmap://map/routeplan?type=walk&fromcoord=CurrentLocation&tocoord=%f,%f&referer=%s";
                String key = getString(R.string.sdk_key);
                if (isInstalled() && latlng != null) {
                    intent.setData(Uri.parse(String.format(uriString, latlng.getLatitude(), latlng.getLongitude(), key)));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "请安装腾讯地图", Toast.LENGTH_LONG).show();
                    Intent download = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pr.map.qq.com/j/tmap/download?key=" + key));
                    startActivity(download);
                }
            }
        });
        button.setEnabled(isBooked);
        buttonPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", index);
                Intent intent = new Intent(MainActivity.this, ParkDetailActivity.class);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
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
                    final Response<Location> locationResponse = locationService.getLocation()
                            .execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.this.updateLocation(locationResponse.body());
                        }
                    });
                } catch (IOException e) {
                    MainActivity.this.log("It has error to request location:" + e.getMessage());
                }
            }
        }, 1000, 3000);
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
    private void log(String message) {
        Log.i(MainActivity.class.getName(), message);
    }

    private void updateLocation(Location location) {
        log("updateLocation: " + location);
        if (marker != null && location.getLat() != 0 && location.getLng() != 0 && location.getLat() >= -90 && location.getLat() <= 90 && location.getLng() >= -180 && location.getLng() <= 180) {
            synchronized (marker) {
                marker.setVisible(true);
                latlng = new LatLng(location.getLat(), location.getLng());
                marker.setPosition(latlng);
                map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            }
        }
    }

    public boolean isInstalled() {
        boolean apkExist = checkApkExist(this, "com.tencent.map");

        return new File("/data/data/com.tencent.map").exists() || apkExist;
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
