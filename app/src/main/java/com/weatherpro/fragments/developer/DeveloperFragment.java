package com.weatherpro.fragments.developer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.weatherpro.MainActivity;
import com.weatherpro.R;

    public class DeveloperFragment extends Fragment {
        MainActivity mainActivity;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
    //            mainActivity.setToolbarTitle(R.string.page_developer);
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_developer, container, false);
        }
    }
