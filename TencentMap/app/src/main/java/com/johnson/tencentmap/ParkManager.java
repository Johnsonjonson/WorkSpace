package com.johnson.tencentmap;

import com.johnson.tencentmap.bean.Park;

import java.util.ArrayList;

public class ParkManager {
    private static ArrayList<Park> parkList = new ArrayList<Park>();

    static {
//        ArrayList<Park> parkList = new ArrayList<Park>();
        for (int i = 1; i < 3; i++) {
            int status = 0;
            int id = i;
            String chepai = "";
            String phone = "";
            boolean isSelect = false;
            Park park = new Park(status, id, chepai, phone,isSelect);

            parkList.add(park);
        }
    }

//     parkList;

    public static ArrayList<Park> getParks(int selectedIndex,int zhanyongIndex){
        for (int i = 1; i < 3; i++) {
            Park park = parkList.get(i-1);
            int id = i;
            if (id == selectedIndex){
                park.setSelect(true);
            }
            if (id == zhanyongIndex){
                park.setStatus(3);
            }
        }
        return  parkList;
    }


}
