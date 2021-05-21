package com.johnson.wifimanager;

public class Address {
    private String url;
    private String ip;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Address(String url, String ip) {
        this.url = url;
        this.ip = ip;
    }
}
