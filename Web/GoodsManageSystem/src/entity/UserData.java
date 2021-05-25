package entity;

import java.util.ArrayList;

public class UserData {
    private ArrayList<Integer> bloodList;
    private ArrayList<Integer> heartList;
    private ArrayList<String> timeList;

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public void setTimeList(ArrayList<String> timeList) {
        this.timeList = timeList;
    }

    public ArrayList<Integer> getBloodList() {
        return bloodList;
    }

    public void setBloodList(ArrayList<Integer> bloodList) {
        this.bloodList = bloodList;
    }

    public ArrayList<Integer> getHeartList() {
        return heartList;
    }

    public void setHeartList(ArrayList<Integer> heartList) {
        this.heartList = heartList;
    }
}
