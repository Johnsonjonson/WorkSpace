package com.johnson.fingerprintpunch.service;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DakaService {
    /**
     * 查询打卡数据
     * @param id 用户id
     * @return
     */
    @POST("query?")
    Call<String> getData(@Query("id") int id);
}

