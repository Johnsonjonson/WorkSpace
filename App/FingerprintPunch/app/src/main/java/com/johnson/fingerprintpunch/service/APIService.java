package com.johnson.fingerprintpunch.service;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @GET("get")
    Call<ResponseBody> getData();

    /**
     * 登录
     * @param id 用户id
     * @return
     */
    @POST("query_user?")
    Call<String> queryUser(@Query("id") int id);

    /**
     * 查询所有学生
     * @return
     */
    @POST("getAllUser?")
    Call<String> getAllUser();

    /**
     * 添加学生
     * @param name 用户名
     * @param tel 手机号码
     * @return
     */
    @POST("addStudent?")
    Call<String> add(@Query("name") String name,
                        @Query("tel") String tel);
}

