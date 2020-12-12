package com.weatherpro.fragments.location;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weatherpro.R;
import com.weatherpro.models.cities.CityModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> implements Filterable {
        private static final String TAG = "RECYCLER_ADAPTER";

        private List<CityModel> cityModelList;
        private List<CityModel> cityModelListAll;
        private OnItemClickListener itemClickListener;  // Слушатель будет устанавливаться извне

        public interface OnItemClickListener {
            void onItemClick(int lat, int lon, String cityName);
        }

        // Сеттер слушателя нажатий
        public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public RecyclerAdapter(List<CityModel> cityModelList) {
            this.cityModelList = cityModelList;
            this.cityModelListAll = new ArrayList<>(cityModelList);
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.row_item, parent, false);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            CityModel cityItem = cityModelList.get(position);
            String cityName = cityItem.getName();
            int lat = (int) Math.round(cityItem.getCoord().getLat());
            int lon = (int) Math.round(cityItem.getCoord().getLon());

            holder.getLocationCity().setText(cityName);
            holder.getLocationCity().setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(lat, lon, cityName);
                }
            });
        }

        @Override
        public int getItemCount() {
            return cityModelList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return R.layout.row_item;
        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<CityModel> filteredList = new ArrayList<>();

                if (charSequence.toString().isEmpty()) {
                    filteredList.addAll(cityModelListAll);
                } else {
                    for (CityModel item :
                            cityModelListAll) {
                        if (item.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cityModelList.clear();
                cityModelList.addAll((Collection<? extends CityModel>) results.values);
                notifyDataSetChanged();
                ;
            }
        };

    }
