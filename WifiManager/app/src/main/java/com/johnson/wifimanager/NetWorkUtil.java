package com.johnson.wifimanager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by JanRoid on 2016/6/28.
 */
public class NetWorkUtil {

    private int key;
    /**
     * 检测网络是否连通
     * @param key
     * @param value
     */
    public void checkNetWork(int key, String value){
        JSONObject jsonData = null;
        String url = null;
        this.key = key;
        try{
            jsonData = new JSONObject(value);
            url = jsonData.getString("url");
        }catch (Exception e){
            e.printStackTrace();
        }
        final String pingUrl = url;
        if(null != url){
            new Thread(){
                @Override
                public void run() {
                    pingUrl(pingUrl);
                }
            }.start();
        }
    }

    /**
     * 对url执行ping操作。
     * @param url
     * @return
     */
    public static String pingUrl(String url){
        String resault = "";
        Process p;
        InputStream input = null;
        BufferedReader in = null;
        try {
            //ping -c 3 -w 100  中  ，-c 是指ping的次数 3是指ping 3次 ，-w 100  以秒为单位指定超时间隔，是指超时时间为100秒
//            p = Runtime.getRuntime().exec("ping -c 2 -w 2 " + url);
            p = Runtime.getRuntime().exec("ping " + url);
            int status = p.waitFor();

            input = p.getInputStream();
            in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            System.out.println("Return ============" + buffer.toString());
            if (status == 0) {
                resault = "success";
            } else {
                resault = "faild";
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null){
                    in.close();
                }

                if (input != null){
                    input.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
//        callLua(resault);
        return url;
    }

    public void callLua(int status){
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("result", status);
//            LuaCallManager.callLua(key, jsonObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
