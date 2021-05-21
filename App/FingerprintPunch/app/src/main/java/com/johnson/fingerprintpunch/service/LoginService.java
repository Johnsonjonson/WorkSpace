package com.johnson.fingerprintpunch.service;

import com.johnson.fingerprintpunch.bean.UserBean;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoginService {
    @FormUrlEncoded
    @POST("login2")
    Call<UserBean> login2(@FieldMap Map<String,String> params);

    /**
     * 登录
     * @param name 用户名
     * @param pwd 密码
     * @return
     */
    @POST("login?")
    Call<String> login(@Query("name") String name,
                       @Query("pwd") String pwd);

    /**
     * 老师登录
     * @param name 用户名
     * @param pwd 密码
     * @return
     */
    @POST("login_teacher?")
    Call<String> loginTeracher(@Query("name") String name,
                       @Query("pwd") String pwd);
}

