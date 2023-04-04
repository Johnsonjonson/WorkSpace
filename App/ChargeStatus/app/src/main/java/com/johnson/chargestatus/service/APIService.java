package com.johnson.chargestatus.service;


import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    /**
     * 获取信息
     * @return
     */
    @GET("get?")
    Call<String> getInfo();
}

