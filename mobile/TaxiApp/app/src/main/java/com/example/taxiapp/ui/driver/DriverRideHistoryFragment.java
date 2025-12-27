package com.example.taxiapp.ui.driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.shared.RideHistoryFragment;

public class DriverRideHistoryFragment extends Fragment {

    public DriverRideHistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_ride_history, container, false);

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new RideHistoryFragment())
                    .commit();
        }

        return view;
    }
}