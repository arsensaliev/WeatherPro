package com.weatherpro.fragments;

import android.annotation.SuppressLint;
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

        getCurrentWeather(view);
    }


    private void getCurrentWeather(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherRequest weatherRequest = retrofit.create(WeatherRequest.class);
        Call<CurrentApi> currentApiCall = weatherRequest.getCurrentData(mainActivity.getLat(), mainActivity.getLon(), Constants.API_KEY);

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

                    String temp = String.valueOf((int) current.getTemp());
                    String windSpeed = String.valueOf((int) current.getWindSpeed());
                    String press = String.valueOf(current.getPressure());
                    String hum = String.valueOf(current.getHumidity());

                    updateViews(temp, windSpeed, press, hum);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CurrentApi> call, @NotNull Throwable t) {
                Snackbar.make(view, Objects.requireNonNull(t.getMessage()), Snackbar.LENGTH_LONG).show();
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void updateViews(String temp, String windSpeed, String press, String hum) {
        temperatureView.setText(temp);
        windView.setText(windSpeed);
        pressureView.setText(press);
        humidityView.setText(hum);
        cityTextView.setText(mainActivity.getCityName());
    }
}