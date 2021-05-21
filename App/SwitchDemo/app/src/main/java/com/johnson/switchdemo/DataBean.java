package com.johnson.switchdemo;

import org.json.JSONException;
import org.json.JSONObject;

public class DataBean {
    private String time;
    private String kaiguan;

    public DataBean(String kaiguan, String time) {
        this.time = time;
        this.kaiguan = kaiguan;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKaiguan() {
        return kaiguan;
    }

    public void setKaiguan(String kaiguan) {
        this.kaiguan = kaiguan;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("time", this.time);
            jsonObject.put("kaiguan", this.kaiguan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
