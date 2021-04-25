package com.johnson.tencentmap;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LocationService {
    @GET("/location")
    Call<Location> getLocation();
}
