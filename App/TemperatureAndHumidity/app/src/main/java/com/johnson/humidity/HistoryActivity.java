package com.johnson.humidity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;

import com.johnson.humidity.bean.DataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private String mFilename = new File(Environment.getExternalStorageState() + "/json/data.txt").getName();
    private DataManager dataManager;
    private ArrayList<DataBean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_title);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ListView listView = findViewById(R.id.lv_data);
        dataManager = DataManager.getInstance(this);
        dataList = dataManager.getDataList();
        DataAdapter adapter=new DataAdapter(HistoryActivity.this,R.layout.daka_item,dataList);
        listView.setAdapter(adapter);
    }

}