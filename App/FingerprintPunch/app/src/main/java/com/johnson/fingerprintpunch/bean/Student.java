package com.johnson.fingerprintpunch.bean;

public class Student {
    private String name;
    private String pwd;
    private int id;
    private String tel;
    private float political;
    private float math;
    private float english;
    private float autoctrl;
    private int daka_num;

    public Student(String name, String pwd, int id, String tel, float political, float math, float english, float autoctrl, int daka_num) {
        this.name = name;
        this.pwd = pwd;
        this.id = id;
        this.tel = tel;
        this.political = political;
        this.math = math;
        this.english = english;
        this.autoctrl = autoctrl;
        this.daka_num = daka_num;


    }
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", id=" + id +
                ", tel='" + tel + '\'' +
                ", political=" + political +
                ", math=" + math +
                ", english=" + english +
                ", autoctrl=" + autoctrl +
                ", daka_num=" + daka_num +
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

    public void setDaka_num(int num) {
        daka_num = num;
    }
}
