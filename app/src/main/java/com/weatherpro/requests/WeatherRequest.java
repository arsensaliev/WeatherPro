package com.weatherpro.requests;

import com.weatherpro.models.current.CurrentApi;
import com.weatherpro.models.forecast.ForecastApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRequest {

    @GET("weather?units=metric")
    Call<CurrentApi> getCurrentData(@Query("lat") int lat, @Query("lon") int lon, @Query("appid") String apiKey);

    @GET("forecast/daily?units=metric")
    Call<ForecastApi> getForecastData(@Query("lat") int lat, @Query("lon") int lon, @Query("cnt") int cnt, @Query("appid") String apiKey);
}
