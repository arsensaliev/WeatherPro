package com.weatherpro.requests;

import com.weatherpro.models.currentApi.CurrentApi;
import com.weatherpro.models.dailyApi.DailyApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRequest {

    @GET("onecall?units=metric&exclude=minutely,hourly,daily&lang=ru")
    Call<CurrentApi> getCurrentData(@Query("lat") int lat, @Query("lon") int lon, @Query("appid") String apiKey);

    @GET("onecall?units=metric&exclude=minutely,hourly,current&lang=ru")
    Call<DailyApi> getDailyData(@Query("lat") int lat, @Query("lon") int lon, @Query("appid") String apiKey);
}
