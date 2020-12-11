package com.weatherpro.requests;

import com.weatherpro.models.CurrentApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRequest {

    @GET("weather")
    Call<CurrentApi> getCurrentData(@Query("lat") int lat, @Query("lon") int lon, @Query("appid") String apiKey);
}
