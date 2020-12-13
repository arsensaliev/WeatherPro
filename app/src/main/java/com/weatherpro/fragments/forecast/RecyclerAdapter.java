package com.weatherpro.fragments.forecast;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherpro.R;
import com.weatherpro.models.dailyApi.Daily;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private static final String TAG = "RECYCLER_ADAPTER";
    private String[] days;

    private List<Daily> dailyList;

    public RecyclerAdapter(String[] days, List<Daily> dailyList) {
        this.days = days;
        this.dailyList = dailyList;
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
        Log.d(TAG, dailyList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return days.length;
    }
}
