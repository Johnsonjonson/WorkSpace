package com.johnson.wifimanager;

import com.hacknife.wifimanager.IWifi;
import com.hacknife.wifimanager.IWifiManager;
import com.hacknife.wifimanager.OnWifiChangeListener;
import com.hacknife.wifimanager.OnWifiConnectListener;
import com.hacknife.wifimanager.OnWifiStateChangeListener;

import java.util.List;

public class WifiManager implements IWifiManager {
    @Override
    public boolean isOpened() {
        return false;
    }

    @Override
    public void openWifi() {

    }

    @Override
    public void closeWifi() {

    }

    @Override
    public void scanWifi() {

    }

    @Override
    public boolean disConnectWifi() {
        return false;
    }

    @Override
    public boolean connectEncryptWifi(IWifi wifi, String password) {
        return false;
    }

    @Override
    public boolean connectSavedWifi(IWifi wifi) {
        return false;
    }

    @Override
    public boolean connectOpenWifi(IWifi wifi) {
        return false;
    }

    @Override
    public boolean removeWifi(IWifi wifi) {
        return false;
    }

    @Override
    public List<IWifi> getWifi() {
        return null;
    }

    @Override
    public void setOnWifiConnectListener(OnWifiConnectListener onWifiConnectListener) {

    }

    @Override
    public void setOnWifiStateChangeListener(OnWifiStateChangeListener onWifiStateChangeListener) {

    }

    @Override
    public void setOnWifiChangeListener(OnWifiChangeListener onWifiChangeListener) {

    }

    @Override
    public void destroy() {

    }
}
