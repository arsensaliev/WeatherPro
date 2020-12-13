package com.weatherpro.fragments.forecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.weatherpro.Constants;
import com.weatherpro.R;
import com.weatherpro.models.forecast.ForecastApi;
import com.weatherpro.models.forecast.List;
import com.weatherpro.requests.WeatherRequest;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastFragment extends Fragment {
    private static final String TAG = "FORECAST_FRAGMENT";
    private String[] days;
    private RecyclerAdapter recyclerAdapter;
    private java.util.List<List> list;
    private int lat;
    private int lon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        loadData();
        getForecastWeather(view);
        // initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.forecast_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        getCurrentDate();

        recyclerAdapter = new RecyclerAdapter(days, list);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
        days = new String[Constants.FORECAST_DEFAULT_DAY];
        for (int i = 0; i < Constants.FORECAST_DEFAULT_DAY; i++) {
            days[i] = simpleDateFormat.format(new Date(date.getTime() + (i + 1) * 24 * 60 * 60 * 1000));
            Log.d(TAG, days[i]);
        }
    }

    private void getForecastWeather(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherRequest weatherRequest = retrofit.create(WeatherRequest.class);
        Call<ForecastApi> forecastApiCall = weatherRequest.getForecastData(lat, lon, Constants.FORECAST_DEFAULT_DAY, Constants.API_KEY);
        forecastApiCall.enqueue(new Callback<ForecastApi>() {
            @Override
            public void onResponse(@NotNull Call<ForecastApi> call, @NotNull Response<ForecastApi> response) {
                if (response.code() == 404) {
                    Snackbar.make(view, "Not Found", Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "Please enter a valid Location");
                } else if (!(response.isSuccessful())) {
                    Snackbar.make(view, response.code(), Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, String.valueOf(response.code()));
                }

                ForecastApi data = response.body();

                if (data != null) {
                    list = data.getList();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ForecastApi> call, @NotNull Throwable t) {
                Snackbar.make(view, Objects.requireNonNull(t.getMessage()), Snackbar.LENGTH_LONG).show();
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        lat = sharedPreferences.getInt(Constants.SHARED_COUNTRY_LAT, Constants.SHARED_COUNTRY_LAT_DEFAULT);
        lon = sharedPreferences.getInt(Constants.SHARED_COUNTRY_LON, Constants.SHARED_COUNTRY_LON_DEFAULT);
    }
}
