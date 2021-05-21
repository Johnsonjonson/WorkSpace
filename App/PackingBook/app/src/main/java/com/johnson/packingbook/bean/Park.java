package com.johnson.packingbook.bean;

public class Park {
    private int status;
    private int id;
    private long time;
    private int userId;
    private String name;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Park(int status, int id, long time) {
        this.status = status;
        this.id = id;
        this.time = time;
    }

    public Park(int status, int id, long time, int userId, String name) {
        this.status = status;
        this.id = id;
        this.time = time;
        this.userId = userId;
        this.name = name;
    }
}
