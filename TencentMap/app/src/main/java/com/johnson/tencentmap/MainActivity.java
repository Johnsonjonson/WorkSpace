package com.johnson.tencentmap;

import android.content.Intent;
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

    public static final String BASE_URL = "http://119.3.54.225:8991";

    private MapView mapView;
    private TencentMap map;

    private LocationService locationService;
    private Timer timer;
    private Marker marker;
    private Button button;
    private LatLng latlng;

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

    public static boolean isInstalled() {
        return new File("/data/data/com.tencent.map").exists();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = this.findViewById(R.id.mapview);
        button = this.findViewById(R.id.button);
        map = mapView.getMap();
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setIndoorLevelPickerEnabled(true);
        map.setMapStyle(TencentMap.MAP_TYPE_NONE);
        final LatLng latLng = new LatLng(0, 0);
        MarkerOptions markerOptions = new MarkerOptions(latLng);
        marker = map.addMarker(markerOptions);
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
}
