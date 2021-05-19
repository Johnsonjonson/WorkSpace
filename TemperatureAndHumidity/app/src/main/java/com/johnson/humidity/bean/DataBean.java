package com.johnson.humidity.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class DataBean {
    private double wendu;
    private double shidu;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataBean(double wendu, double shidu,String time) {
        this.wendu = wendu;
        this.shidu = shidu;
        this.time = time;
    }

    public double getWendu() {
        return wendu;
    }

    public void setWendu(double wendu) {
        this.wendu = wendu;
    }

    public double getShidu() {
        return shidu;
    }

    public void setShidu(double shidu) {
        this.shidu = shidu;
    }
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wendu", this.wendu);
            jsonObject.put("shidu", this.shidu);
            jsonObject.put("time", this.time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
