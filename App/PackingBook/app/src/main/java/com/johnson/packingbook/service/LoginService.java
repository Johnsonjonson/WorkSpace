package com.johnson.packingbook.service;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {

    /**
     * 登录
     * @param name 用户名
     * @param pwd 密码
     * @return
     */
    @POST("login?")
    Call<String> login(@Query("name") String name,
                       @Query("pwd") String pwd);

}

