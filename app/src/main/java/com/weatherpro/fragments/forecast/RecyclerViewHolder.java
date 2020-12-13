package com.weatherpro.fragments.forecast;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private TextView temperatureView;
    private TextView dateView;
    private TextView descriptionView;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
