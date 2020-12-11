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
import com.weatherpro.models.CurrentApi;
import com.weatherpro.models.Main;
import com.weatherpro.models.Wind;
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

    MainActivity mainActivity;

    // Views
    TextView temperatureView;
    TextView windView;
    TextView humidityView;
    TextView pressureView;
    Retrofit retrofit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        init(view);
        getCurrentWeather(view);
        return view;
    }

    private void init(View view) {
        temperatureView = view.findViewById(R.id.main_fragment_current_temperature);
        windView = view.findViewById(R.id.main_fragment_current_wind);
        humidityView = view.findViewById(R.id.main_fragment_current_humidity);
        pressureView = view.findViewById(R.id.main_fragment_current_pressure);

        getCurrentWeather(view);
    }


    private void getCurrentWeather(View view) {


        WeatherRequest weatherRequest = retrofit.create(WeatherRequest.class);
        Call<CurrentApi> currentApiCall = weatherRequest.getCurrentData(55, 37, Constants.API_KEY);

        currentApiCall.enqueue(new Callback<CurrentApi>() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onResponse(@NotNull Call<CurrentApi> call, @NotNull Response<CurrentApi> response) {
                if (response.code() == 404) {
                    Snackbar.make(view, "", Snackbar.LENGTH_LONG).show();
//                    Log.d(TAG, "Please enter a valid Location");
                } else if (!(response.isSuccessful())) {
                    Snackbar.make(view, response.code(), Snackbar.LENGTH_LONG).show();
//                    Log.d(TAG, String.valueOf(response.code()));
                }


                CurrentApi data = response.body();

                if (data != null) {
                    Main main = data.getMain();
                    Wind wind = data.getWind();

                    int temperature = (int) (main.getTemp() - 273.15);

                    temperatureView.setText(String.valueOf(temperature));
                    windView.setText(Double.toString(wind.getSpeed()));
                    humidityView.setText(String.valueOf(main.getHumidity()));
                    pressureView.setText(String.valueOf(main.getPressure()));
                }
            }

            @Override
            public void onFailure(@NotNull Call<CurrentApi> call, @NotNull Throwable t) {
                Snackbar.make(view, Objects.requireNonNull(t.getMessage()), Snackbar.LENGTH_LONG).show();
                Log.d(TAG, t.getMessage());
            }
        });
    }
}