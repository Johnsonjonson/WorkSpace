package com.johnson.wifimanager;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/1/2 0002.
 */
public class WifiUtils {
    private static final String TAG = "WifiAdmin";
    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;
    // 定义一个WifiLock
    WifiManager.WifiLock mWifiLock;

    // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    // 构造器
    public WifiUtils(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
    }

    // 打开WIFI
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    // 关闭WIFI
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    // 检查当前WIFI状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    public WifiManager getWifiManager() {
        return mWifiManager;
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    // 指定配置好的网络进行连接
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

    public void startScan() {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    //获取连接过的wifi信息
    public List<WifiConfiguration> getLinkList() {
        return mWifiManager.getConfiguredNetworks();
    }

    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    // 查看扫描结果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    // 得到MAC地址
    public String getMacAddress() {
        // 取得WifiInfo对象
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    // 得到接入点的SSID
    public String getSSID() {
        // 取得WifiInfo对象
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
    }

    // 得到接入点的BSSID
    public String getBSSID() {
        // 取得WifiInfo对象
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    // 得到IP地址
    public int getIPAddress() {
        // 取得WifiInfo对象
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得到连接的ID
    public int getNetworkId() {
        // 取得WifiInfo对象
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到WifiInfo的所有信息包
    public String getWifiInfo() {
        // 取得WifiInfo对象
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    private int getNetworkId(String wifiName){
        List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + wifiName + "\"")) {
                return i.networkId;
            }
        }
        return -1;
    }

    public boolean addNetwork(WifiConfiguration wifiInfo, String ssid) {
        int wcgID = mWifiManager.addNetwork(wifiInfo);
        ssid = "\""+ssid+"\"";
        int networkId = getNetworkId(ssid);
        Log.d("Johnson",ssid+"        "+networkId);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        System.out.println("a--" + wcgID);
        System.out.println("b--" + b);
        return b;
    }

    // 添加一个网络并连接
    public boolean addNetwork(WifiConfiguration wcg) {
//        "ChinaNet-Hqdr"
        int wcgID = mWifiManager.addNetwork(wcg);
        String ssid = "\""+wcg.SSID+"\"";
        int networkId = getNetworkId(ssid);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        System.out.println("a--" + wcgID);
        System.out.println("b--" + b);
        return b;
    }

    // 断开指定ID的网络
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

//然后是一个实际应用方法，只验证过没有密码的情况：

    public WifiConfiguration createWifiInfo(String SSID, String password, WifiCipherType type) {

        Log.v(TAG, "SSID = " + SSID + "## Password = " + password + "## Type = " + type);

        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
//        config.SSID = SSID ;
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.isExsits(SSID);
        if (tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        // 分为三种情况：1没有密码2用wep加密3用wpa加密
        if (type == WifiCipherType.WIFICIPHER_NOPASS) {// WIFICIPHER_NOPASS
//            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;

        } else if (type == WifiCipherType.WIFICIPHER_WEP) {  //  WIFICIPHER_WEP
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == WifiCipherType.WIFICIPHER_WPA) {   // WIFICIPHER_WPA
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }

        return config;
    }

    public WifiConfiguration getWifiConfig(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("/" + SSID + "/")) {
                return existingConfig;
            }
        }
        return null;
    }

    public boolean delWifi(String ssid) {
        WifiConfiguration tempConfig = isExsits(ssid);

        if (tempConfig != null) {
//            wifiManager.enableNetwork(tempConfig.networkId,false);//无效
            tempConfig.hiddenSSID = true;
            tempConfig.wepKeys[0] = "\"" + "1234" + "\"";
            tempConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            tempConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            tempConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            tempConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            tempConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            tempConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            tempConfig.wepTxKeyIndex = 0;

            //tempConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            //tempConfig.hiddenSSID = true;
            mWifiManager.updateNetwork(tempConfig);
            mWifiManager.removeNetwork(tempConfig.networkId);
            return true;
        }

        return false;
    }

    // 查看以前是否也配置过这个网络
    public WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }


    public boolean connect(String ssid, String password, WifiCipherType type) {
        WifiConfiguration wifiConfiguration = createWifiInfo(ssid, password, type);
        if (wifiConfiguration == null) {
            return false;
        }
        return addNetwork(wifiConfiguration);


//        try {
//            WifiConfiguration wifiConfig = createWifiInfo(ssid, password, type);
//            //
//            if (wifiConfig == null) {
//                Log.d(TAG, "wifiConfig is null!");
//                return false;
//            }
//
//        WifiConfiguration tempConfig = getWifiConfig(ssid);
//        if (tempConfig != null) {
//            mWifiManager.removeNetwork(tempConfig.networkId);
//        }
//
//            int netID = mWifiManager.addNetwork(wifiConfig);
//            Log.d(TAG,"net id:" + netID);
//            boolean enabled = mWifiManager.enableNetwork(netID, true);
//            Log.d(TAG, "enableNetwork status enable=" + enabled);
//            boolean connected = mWifiManager.reconnect();
//            Log.d(TAG, "enableNetwork connected=" + connected);
//            if (netID==-1) {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG,e.getMessage());
//            return false;
//        }
//        return true;
    }

    private static boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();

        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }

        return isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f')) {
                return false;
            }
        }
        return true;
    }


    //增加部分
    private static StringBuffer header;

//    public static List<NetWork> listAllWifiDate() {
//        LogUtil.w("test", "listallwifi@@");
//        try {
//            List<String> strs = RootTools.sendShell("cat data/misc/wifi/wpa_supplicant.conf", 31000);
//            if (strs == null) return null;
//
//            header = new StringBuffer();
//            List<NetWork> list = new ArrayList<NetWork>();
//            boolean isStarted = false;
//            for (String line : strs) {
//                if (line == null) {
//                    continue;
//                }
//                //Log.w("test","line:" + line);
//
//                if (line.replace(" ", "").equals("network={")) {
//                    isStarted = true;
//                    temp = new NetWork();
////                    temp.setLatitude(APPLocation.getLatitude(null));
////                    temp.setLontitude(APPLocation.getLontitude(null));
////                    temp.setAddr(APPLocation.getAddress(null));
//                } else if (line.replace(" ", "").equals("}")) {
//                    isStarted = false;
//                    list.add(temp);
//                } else if (isStarted) {
//                    String[] kv = line.trim().split("=");
//                    if (kv.length > 1) {
//                        String key = kv[0];
//                        String value = kv[1];
//
//                        if (value != null) {
//                            value = value.trim();
//                            if (value.length() > 2) {
//                                if (value.charAt(0) == '"') {
//                                    value = value.substring(1, value.length() - 1);
//                                }
//                                if (value.charAt(value.length() - 1) == '"') {
//                                    value = value.substring(0, value.length() - 2);
//                                }
//                            }
//                        }
//
//                        if (key.equals("ssid")) {
//                            temp.setSsid(value);
//                        } else if (key.equals("key_mgmt")) {
//                            temp.setKey_mgmt(value);
//                        } else if (key.equals("psk")) {
//                            temp.setPsk(value);
//                            temp.setShowpsk(getShowPsk(value));
//                        } else if (key.equals("priority")) {
//                            temp.setPriority(value);
//                        } else if (key.equals("auth_alg")) {
//                            temp.setAuth_alg(value);
//                        } else if (key.equals("eapol_flag")) {
//                            temp.setEapol_flags(value);
//                        } else if (key.equals("group")) {
//                            temp.setGroup(value);
//                        } else if (key.equals("pairwise")) {
//                            temp.setPairwise(value);
//                        } else if (key.equals("proto")) {
//                            temp.setProto(value);
//                        } else if (key.equals("private_key")) {
//                            temp.setPrivate_key(value);
//                        } else {
//                            //Log.i("test","error:"  + key + ">>" + value);
//                        }
//                    }
//                } else if (!isStarted) {
//                    header.append(line + "\n");
//                }
//            }
//
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static String getShowPsk(String psk) {
        if (psk == null) {
            return "";
        }
        char head = 0, end = 0;
        int len;
        if (psk.length() > 2) {
            head = psk.charAt(0);
            end = psk.charAt(psk.length() - 1);

            len = psk.length() - 2;
        } else {
            len = psk.length();
        }

        StringBuffer sb = new StringBuffer();
        if (head != 0)
            sb.append(head);

        for (int i = 0; i < len; i++) {
            sb.append("*");
        }


        if (end != 0)
            sb.append(end);

        return sb.toString();
    }
}