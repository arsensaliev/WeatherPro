package com.weatherpro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.weatherpro.fragments.MainFragment;
import com.weatherpro.fragments.developer.DeveloperFragment;
import com.weatherpro.fragments.forecast.ForecastFragment;
import com.weatherpro.fragments.location.LocationFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView drawerHeaderTextView;
    // Fragments
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;

    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        drawerHeaderTextView = navigationView.getHeaderView(0).findViewById(R.id.drawer_header_text);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        loadCityName();
        pushFragments(new MainFragment(), null);
    }

    private OnQueryTextListener queryTextListener;

    public void setQueryTextListener(OnQueryTextListener queryTextListener) {
        this.queryTextListener = queryTextListener;
    }

    public interface OnQueryTextListener {
        boolean onQueryTextSubmit(String query);

        boolean onQueryTextChange(String newText);
    }

    public void pushFragments(Fragment fragment, Bundle bundle) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (queryTextListener != null) {
                    queryTextListener.onQueryTextSubmit(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (queryTextListener != null) {
                    queryTextListener.onQueryTextChange(newText);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.home:
                fragment = new MainFragment();
                pushFragments(fragment, null);
                toolbar.getMenu().findItem(R.id.app_bar_search).setVisible(false);
                break;
            case R.id.developer:
                fragment = new DeveloperFragment();
                pushFragments(fragment, null);
                toolbar.getMenu().findItem(R.id.app_bar_search).setVisible(false);
                break;
            case R.id.location:
                fragment = new LocationFragment();
                pushFragments(fragment, null);
                toolbar.getMenu().findItem(R.id.app_bar_search).setVisible(true);
                break;

            case R.id.forecast:
                fragment = new ForecastFragment();
                pushFragments(fragment, null);
                toolbar.getMenu().findItem(R.id.app_bar_search).setVisible(false);
                break;
            default:
                fragment = new MainFragment();
                pushFragments(fragment, null);
        }
        return true;
    }

    public void loadCityName() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MAIN_SHARED_NAME, Context.MODE_PRIVATE);
        cityName = sharedPreferences.getString(Constants.SHARED_COUNTRY_NAME, Constants.SHARED_COUNTRY_NAME_DEFAULT);
        drawerHeaderTextView.setText(cityName);
    }
}