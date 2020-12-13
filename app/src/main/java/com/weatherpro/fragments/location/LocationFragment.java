package com.weatherpro.fragments.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.weatherpro.Constants;
import com.weatherpro.MainActivity;
import com.weatherpro.R;
import com.weatherpro.fragments.MainFragment;
import com.weatherpro.models.cities.CitiesModel;
import com.weatherpro.models.cities.CityModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class LocationFragment extends Fragment {
    private static final String TAG = "LOCATION_FRAGMENT";

    private MainActivity mainActivity;
    private RecyclerAdapter recyclerAdapter;
    private List<CityModel> cityModelList;
    private SearchView searchView;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        init(view);
        return view;
    }

    @SuppressLint("CommitPrefEdits")
    private void init(View view) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initRecyclerView(view);
        initSearchListener();
    }

    private void initSearchListener() {
        mainActivity.setQueryTextListener(new MainActivity.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_location);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Gson gson = new Gson();
        String cities = getCities();
        cityModelList = gson.fromJson(cities, CitiesModel.class).getList();

        recyclerAdapter = new RecyclerAdapter(cityModelList);
        recyclerAdapter.SetOnItemClickListener((lat, lon, cityName) -> {
            editor.putBoolean(Constants.SHARED_IS_COUNTRY_EMPTY, false);
            editor.putString(Constants.SHARED_COUNTRY_NAME, cityName);
            editor.putInt(Constants.SHARED_COUNTRY_LAT, lat);
            editor.putInt(Constants.SHARED_COUNTRY_LON, lon);
            editor.apply();
            mainActivity.pushFragments(new MainFragment(), null);
            mainActivity.loadCityName();
        });
        recyclerView.setAdapter(recyclerAdapter);
    }

    private String getCities() {
        StringBuilder buf = new StringBuilder();
        InputStream json = null;
        try {
            json = getContext().getAssets().open(Constants.FILE_CITIES);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(json, StandardCharsets.UTF_8));
        String str = null;

        while (true) {
            try {
                if ((str = in.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            buf.append(str);
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
}
