package entity;

public class HealthDataBean {
    private String time;
    private int blood;
    private int heart;

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public HealthDataBean(String time, int blood,int heart) {
        this.time = time;
        this.blood = blood;
        this.heart = heart;
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
