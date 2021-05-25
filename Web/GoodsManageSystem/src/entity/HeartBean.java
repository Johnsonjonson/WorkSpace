package entity;

public class HeartBean {
    private String time;
    private int heart;

    public HeartBean(String time, int heart) {
        this.time = time;
        this.heart = heart;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }
}
