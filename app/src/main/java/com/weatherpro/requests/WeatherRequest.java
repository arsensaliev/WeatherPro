package com.weatherpro.requests;

import com.weatherpro.models.current.CurrentApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

    public interface WeatherRequest {

        @GET("weather?units=metric")
        Call<CurrentApi> getCurrentData(@Query("lat") int lat, @Query("lon") int lon, @Query("appid") String apiKey);
    }
