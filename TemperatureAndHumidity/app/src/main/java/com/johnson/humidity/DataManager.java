package com.johnson.humidity;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class DataManager {
    private static DataManager m_instance;
    private final Context mCotext;
    private ArrayList<DataBean> dataList;

    public DataManager(Context context) {
        mCotext = context;
        try {
            dataList = loadDatas();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static DataManager getInstance(Context context){
        if (m_instance == null){
            m_instance = new DataManager(context);
        }
        return m_instance;
    }

    private String mFilename = new File(Environment.getExternalStorageState() + "/json/data.txt").getName();

    public void saveDatas() throws JSONException,
            IOException {
        JSONArray array = new JSONArray();
        if(dataList == null){
            return;
        }
        for (DataBean c : dataList)
            array.put(c.toJSON());

        Writer writer = null;
        try {
            OutputStream out = mCotext.openFileOutput(mFilename,
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            Log.d("Johnson", array.toString());
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<DataBean> loadDatas() throws IOException, JSONException {
        ArrayList<DataBean> datas = new ArrayList<DataBean>();
        BufferedReader reader = null;
        try {
            InputStream in = mCotext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            Log.d("Johnson", jsonString.toString());
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                double wendu = jsonObject.optDouble("wendu", 0);
                double shidu = jsonObject.optDouble("shidu", 0);
                String time = jsonObject.optString("time", "");
                if(datas.size() >= 50){
                    datas.remove(0);
                }
                datas.add(new DataBean(wendu, shidu,time));
            }
        } catch (FileNotFoundException e) {

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return datas;
    }

    public void addData(DataBean dataBean){
        if (dataList != null) {
            if (dataList.size() >= 50) {
                dataList.remove(0);
            }
            dataList.add(dataBean);
        }
    }

    public ArrayList<DataBean> getDataList(){
        return dataList;
    }
}