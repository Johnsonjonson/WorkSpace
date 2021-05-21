package com.johnson.packingbook.service;


import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    /**
     * 查询用户信息
     * @param id 用户id
     * @return
     */
    @POST("query_user?")
    Call<String> queryUser(@Query("id") int id);

    /**
     * 修改密码
     * @param id 用户id
     * @param pwd 用户旧密码
     * @param newpwd 用户新密码
     * @return
     */
    @POST("modify_pwd?")
    Call<String> modifyPwd(@Query("id") int id,@Query("pwd") String pwd,@Query("newpwd") String newpwd);

    /**
     * 获取所有停车位
     * @return
     */
    @POST("query_park?")
    Call<String> getAllPark();


    /**
     * 预约车位
     * @return
     */
    @POST("book_park?")
    Call<String> bookPark(@Query("userId") int userId,
                         @Query("parkId") int parkId);

    /**
     * 取车
     * @return
     */
    @POST("take_car?")
    Call<String> takeCar(@Query("userId") int userId,
                            @Query("parkId") int parkId);
}

