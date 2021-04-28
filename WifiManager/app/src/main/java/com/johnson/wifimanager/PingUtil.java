package com.johnson.wifimanager;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingUtil {

    private static final String TAG = "Johnson";
    private int key;

//    public static String ip = "";

    /**
     * 对url执行ping操作。
     *
     * @param url
     * @return
     */
    public static String pingUrl(String url) {
        String ip = "";
        Log.d(TAG,url);
        Process p;
        InputStream input = null;
        BufferedReader in = null;
        try {
            //ping -c 3 -w 100  中  ，-c 是指ping的次数 3是指ping 3次 ，-w 100  以秒为单位指定超时间隔，是指超时时间为100秒
//            p = Runtime.getRuntime().exec("ping -c 2 -w 2 " + url);
            p = Runtime.getRuntime().exec("ping -c 1 " + url);
            int status = p.waitFor();

            input = p.getInputStream();
            in = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println("Return ============" + buffer.toString());
            if (status == 0) {
                ip = getIps(buffer.toString());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    public static boolean isIPLegal(String ipStr){
        if(TextUtils.isEmpty(ipStr))
            return false;

        Pattern pattern = Pattern.compile("^((http|https)://)((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$");
        Matcher matcher = pattern.matcher(ipStr);
        return matcher.find();
    }

    /**
     * 正则提前字符串中的IP地址
     * @param ipString
     * @return
     */
    public static String getIps(String ipString){
        String regEx="((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(ipString);
        String ip = "";
        if (m.find()) {
            ip = m.group();
        }
        return ip;
    }

}
