package com.johnson.fingerprintpunch.bean;

public class UserBean {
    private String name;
    private String pwd;
    private int id;
    private String tel;
    private float political;
    private float math;
    private float english;
    private float autoctrl;
    private int daka_num;
    /**
     * 0 没有用户
     * 1 登录成功
     * 2 密码错误
     */

    private int errorno;


    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", id=" + id +
                ", tel='" + tel + '\'' +
                ", political=" + political +
                ", math=" + math +
                ", english=" + english +
                ", autoctrl=" + autoctrl +
                ", daka_num=" + daka_num +
                ", errorno=" + errorno +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getTel() {
        return tel;
    }

    public float getPolitical() {
        return political;
    }

    public float getMath() {
        return math;
    }

    public float getEnglish() {
        return english;
    }

    public float getAutoctrl() {
        return autoctrl;
    }

    public int getDaka_num() {
        return daka_num;
    }

    public int getErrorno() {
        return errorno;
    }
}
