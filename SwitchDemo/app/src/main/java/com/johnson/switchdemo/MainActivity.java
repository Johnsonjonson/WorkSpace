package com.johnson.switchdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.johnson.switchdemo.http.OkHttpUtil;

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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ArrayList<DataBean> dataList = new ArrayList<>();
    private String mFilename = new File(Environment.getExternalStorageState() + "/json/data.txt").getName();
    private static Activity mContext;
    private ListView listView;
    private Switch switch1;
    private boolean isGetData;

    public static Context getActivity() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.lv_data);
        switch1 = findViewById(R.id.tool_switch);
        mContext = this;
        isGetData = false;
        try {
            dataList = loadDatas();
            DataAdapter adapter=new DataAdapter(this,R.layout.daka_item,dataList);
            listView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String msg = "0";
                if(b){
                    msg = "1";
                }
                if(!isGetData)
                    sendData(msg);
            }
        });
        getData();
    }

    private void updateListView(boolean b) {
        long timecurrentTimeMillis = System.currentTimeMillis();
        String time = stampToDate(timecurrentTimeMillis+"");
        String kaiguan = "";
        if (b){
            kaiguan = "打开";

        }else{
            kaiguan = "关闭";
        }
        dataList.add(new DataBean(kaiguan,time));
        DataAdapter adapter=new DataAdapter(MainActivity.this,R.layout.daka_item,dataList);
        listView.setAdapter(adapter);
        try {
            saveDatas(dataList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getData(){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.BASE_URL);
                        new Handler(mContext.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                isGetData = true;
                                switch1.setChecked(Integer.parseInt(result)==1);
                                isGetData = false;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendData(String msg){
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String result;
                    try {
                        result = OkHttpUtil.getStringFromServer(OkHttpUtil.attachHttpGetParam(OkHttpUtil.UPDATE_STATUS_URL,"status",msg));
                        new Handler(mContext.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                Log.d("result ===  ",result);
                                updateListView(Integer.parseInt(result)==1);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DataBean> loadDatas() throws IOException, JSONException {
        ArrayList<DataBean> datas = new ArrayList<DataBean>();
        BufferedReader reader = null;
        try {
            InputStream in = openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            Log.d("Johnson",jsonString.toString());
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String kaiguan = jsonObject.optString("kaiguan", null);
                String time = jsonObject.optString("time", "");
                datas.add(new DataBean(kaiguan,time));
            }
        } catch (FileNotFoundException e) {

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return datas;
    }

    public void saveDatas(ArrayList<DataBean> dataList) throws JSONException,
            IOException {
        JSONArray array = new JSONArray();
        for (DataBean c : dataList)
            array.put(c.toJSON());

        Writer writer = null;
        try {
            OutputStream out = this.openFileOutput(mFilename,
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            Log.d("Johnson",array.toString());
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /*
     * 将时间戳转换为时间
     *
     * s就是时间戳
     */

    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


}