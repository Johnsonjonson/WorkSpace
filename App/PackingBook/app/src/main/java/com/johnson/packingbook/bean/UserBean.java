package com.johnson.packingbook.bean;

public class UserBean {
    private String name;
    private String pwd;
    private int id;
    private int status;
    private int parkId;
    private String parkTime;  //停车时间戳
    private String bookTime;// 预约时间

    public String getParkTime() {
        return parkTime;
    }

    public String getBookTime() {
        return bookTime;
    }


    /**
     * 0 没有用户
     * 1 登录成功
     * 2 密码错误
     */

    private int errorno;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    public int getErrorno() {
        return errorno;
    }

    public void setErrorno(int errorno) {
        this.errorno = errorno;
    }
}
