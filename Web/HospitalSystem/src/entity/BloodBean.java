package entity;

public class BloodBean {
    private String time;
    private int blood;

    public BloodBean(String time, int blood) {
        this.time = time;
        this.blood = blood;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = blood;
    }
}
