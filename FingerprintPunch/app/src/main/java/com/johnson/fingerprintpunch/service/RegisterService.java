package com.johnson.fingerprintpunch.service;

import com.johnson.fingerprintpunch.bean.UserBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterService {
    /**
     * 注册
     * @param name 用户名
     * @param pwd 密码
     * @param tel 手机号码
     * @return
     */
    @POST("regist?")
    Call<String> regist(@Query("name") String name,
                       @Query("pwd") String pwd,
                        @Query("tel") String tel);

    /**
     * 老师注册
     * @param name 用户名
     * @param pwd 密码
     * @param tel 手机号码
     * @return
     */
    @POST("regist_teacher?")
    Call<String> registTeacher(@Query("name") String name,
                        @Query("pwd") String pwd,
                        @Query("tel") String tel);
}

