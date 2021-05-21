package com.fishtank.tcp;

public class MessageServer {

    private String result;

    public MessageServer(String data){
        this.result = data;
    }

    public String getResult() {
        return result;
    }
}
