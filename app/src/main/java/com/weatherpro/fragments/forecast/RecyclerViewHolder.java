package com.weatherpro.fragments.forecast;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherpro.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private TextView temperatureView;
    private TextView dateView;
    private TextView descriptionView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        temperatureView = itemView.findViewById(R.id.fragment_forecast_temperature);
        dateView = itemView.findViewById(R.id.fragment_forecast_date);
        descriptionView = itemView.findViewById(R.id.fragment_forecast_description);
    }

    public TextView getTemperatureView() {
        return temperatureView;
    }

    public TextView getDateView() {
        return dateView;
    }

    public TextView getDescriptionView() {
        return descriptionView;
    }

    public void setData(String temperature, String date, String desc) {
        temperatureView.setText(temperature);
        dateView.setText(date);
        descriptionView.setText(desc);
    }
}
