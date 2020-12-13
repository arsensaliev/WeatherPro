package com.weatherpro.fragments.forecast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherpro.R;
import com.weatherpro.models.forecast.List;
import com.weatherpro.models.forecast.Weather;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    String[] days;
    String[] desc;
    java.util.List<List> list;

    public RecyclerAdapter(String[] days, java.util.List<List> list) {
        this.days = days;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_forecast_row_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        List item = list.get(position);
        int temp = item.getTemp().getDay();
        String day = days[position];
        java.util.List<Weather> weather = item.getWeather();
        String desc = weather.get(position).getDescription();
        holder.setData(String.valueOf(temp), days[position], desc);
    }

    @Override
    public int getItemCount() {
        return days.length;
    }
}
