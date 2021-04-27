package com.johnson.responder;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @GET("/get")
    Call<String> getIndex();

    /**
     * setIndex
     * @param index
     * @return
     */
    @POST("update?")
    Call<String> setIndex(@Query("index") String index);
}
