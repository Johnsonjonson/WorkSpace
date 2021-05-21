package com.johnson.tencentmap;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LocationService {
    @GET("/get")
    Call<Location> getLocation();
    @GET("/get")
    Call<Data> getMultiLocation();

    @GET("/get")
    Call<String> getLocations();
}
