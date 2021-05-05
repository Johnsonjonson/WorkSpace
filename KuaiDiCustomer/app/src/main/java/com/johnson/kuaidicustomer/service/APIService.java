package com.johnson.kuaidicustomer.service;


import com.johnson.kuaidicustomer.bean.Express;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    /**
     *
     * @param start  起点
     * @param end    终点
     * @param phone  电话
     * @param name   姓名
     * @return
     */
    @POST("create?")
    Call<String> createExpress(@Query("start") String start,
                               @Query("end") String end,
                               @Query("phone") String phone,
                               @Query("name") String name);
    /**
     * 获取最近一条快递单
     * @return
     */
    @POST("last?")
    Call<String> getLastExpress();


    /**
     * 支付
     * @param id
     * @return
     */
    @POST("pay?")
    Call<String> pay(@Query("id") int id);

    /**
     * 更新物流状态
     * @param id
     * @param status
     * @return
     */
    @POST("update_status?")
    Call<String> updateStatus(@Query("id") int id,@Query("status") int status);
}

