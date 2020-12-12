package com.weatherpro.fragments.location;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherpro.R;

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView locationCity;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            locationCity = itemView.findViewById(R.id.fragment_location_city);
        }



        public TextView getLocationCity() {
            return locationCity;
        }
    }
