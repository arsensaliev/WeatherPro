package com.weatherpro.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.weatherpro.Constants;
import com.weatherpro.MainActivity;
import com.weatherpro.R;
import com.weatherpro.models.currentApi.Current;
import com.weatherpro.models.currentApi.CurrentApi;
import com.weatherpro.requests.WeatherRequest;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {
    private final String TAG = "MAIN_FRAGMENT";

    private MainActivity mainActivity;

    // Views
    private TextView temperatureView;
    private TextView windView;
    private TextView humidityView;
    private TextView pressureView;
    private TextView cityTextView;

    private SharedPreferences.Editor editor;

    private String cityName;
    private int cityLat;
    private int cityLon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        loadData();
        init(view);
        return view;
    }

    @SuppressLint("CommitPrefEdits")
    private void init(View view) {
        temperatureView = view.findViewById(R.id.main_fragment_current_temperature);
        windView = view.findViewById(R.id.main_fragment_current_wind);
        humidityView = view.findViewById(R.id.main_fragment_current_humidity);
        pressureView = view.findViewById(R.id.main_fragment_current_pressure);
        cityTextView = view.findViewById(R.id.main_fragment_location);

        cityTextView.setText(cityName);
        getCurrentWeather(view);
    }


    private void getCurrentWeather(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherRequest weatherRequest = retrofit.create(WeatherRequest.class);
        Call<CurrentApi> currentApiCall = weatherRequest.getCurrentData(cityLat, cityLon, Constants.API_KEY);

        currentApiCall.enqueue(new Callback<CurrentApi>() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onResponse(@NotNull Call<CurrentApi> call, @NotNull Response<CurrentApi> response) {
                if (response.code() == 404) {
                    Snackbar.make(view, "Not Found", Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "Please enter a valid Location");
                } else if (!(response.isSuccessful())) {
                    Snackbar.make(view, response.code(), Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, String.valueOf(response.code()));
                }


                CurrentApi data = response.body();

                if (data != null) {
                    Current current = data.getCurrent();
                    temperatureView.setText(String.valueOf((int) current.getTemp()));
                    windView.setText(String.valueOf(current.getWindSpeed()));
                    pressureView.setText(String.valueOf(current.getPressure()));
                    humidityView.setText(String.valueOf(current.getHumidity()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<CurrentApi> call, @NotNull Throwable t) {
                Snackbar.make(view, Objects.requireNonNull(t.getMessage()), Snackbar.LENGTH_LONG).show();
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        cityName = sharedPreferences.getString(Constants.SHARED_COUNTRY_NAME, Constants.SHARED_COUNTRY_NAME_DEFAULT);
        cityLat = sharedPreferences.getInt(Constants.SHARED_COUNTRY_LAT, Constants.SHARED_COUNTRY_LAT_DEFAULT);
        cityLon = sharedPreferences.getInt(Constants.SHARED_COUNTRY_LON, Constants.SHARED_COUNTRY_LON_DEFAULT);
    }
}