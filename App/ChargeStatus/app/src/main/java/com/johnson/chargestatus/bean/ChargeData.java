package com.johnson.chargestatus.bean;

public class ChargeData {
    private double voltage;  //电压
    private double current; //电流
    private double temp;    //温度
    private double status;    //状态

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }



    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
