package com.johnson.packingbook.manager;

import com.johnson.packingbook.bean.UserBean;

public class UserManager {

    public static UserBean user;
    public static void setUser(UserBean user){
        UserManager.user = user;
    }

    public static UserBean getUser(){
        return UserManager.user;
    }
}
