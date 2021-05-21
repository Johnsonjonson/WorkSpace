package com.johnson.qrcodedemo.bean;

public class Park {
    private int status;
    private int id;
    private String chepai;
    private String phone;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChepai() {
        return chepai;
    }

    public void setChepai(String chepai) {
        this.chepai = chepai;
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


    public Park(int status, int id, String chepai, String phone,boolean isSelect) {
        this.status = status;
        this.id = id;
        this.chepai = chepai;
        this.phone = phone;
        this.isSelect = isSelect;
    }
}
