package com.johnson.securitymonitoringsystem.service;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    /**
     * 获取信息
     * @return
     */
    @GET("get?")
    Call<String> getInfo();

    /**
     * 获取开关
     * @return
     */
    @GET("get_switch?")
    Call<String> getSwitch();

    /**
     * 设置开关
     * @return
     */
    @GET("set_switch?")
    Call<String> setSwitch(@Query("switch") String sw);

}

