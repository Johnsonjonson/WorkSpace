package com.johnson.fingerprintpunch.service;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InputService {
    /**
     * 注册
     * @param id 用户id
     * @param name 用户名
     * @param math 高数
     * @param english 英语
     * @param zhengzhi 政治
     * @param auto 自动化控制
     * @return
     */
    @POST("score?")
    Call<String> input(@Query("id") int id,
                        @Query("name") String name,
                       @Query("math") float math,
                        @Query("english") float english,
                        @Query("zhengzhi") float zhengzhi,
                        @Query("auto") float auto);
}

