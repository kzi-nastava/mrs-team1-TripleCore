package com.example.taxiapp.ui.driver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.shared.RideHistoryFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import model.RideDetailsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.AuthService;
import service.DriverService;

public class DriverRideHistoryFragment extends Fragment {

    private static final String ARG_RIDE_HISTORY = "ride_history";
    private static final String ARG_LOADED = "loaded";

    private List<RideDetailsDTO> rideHistory = new ArrayList<>();
    private boolean isDataLoaded = false;

    public DriverRideHistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            String json = savedInstanceState.getString(ARG_RIDE_HISTORY);
            if (json != null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<RideDetailsDTO>>() {}.getType();
                rideHistory = gson.fromJson(json, listType);
                isDataLoaded = savedInstanceState.getBoolean(ARG_LOADED, false);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!rideHistory.isEmpty()) {
            Gson gson = new Gson();
            String json = gson.toJson(rideHistory);
            outState.putString(ARG_RIDE_HISTORY, json);
            outState.putBoolean(ARG_LOADED, isDataLoaded);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.fragment_driver_ride_history,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isDataLoaded) {
            loadRideHistoryFromBackend();
        } else if (!rideHistory.isEmpty()) {
            openRideHistoryFragment();
        }
    }

    private void loadRideHistoryFromBackend() {
        Long driverId = AuthService.getInstance().getLoggedInUserId(requireContext());

        DriverService driverService = DriverService.getInstance();
        driverService.getDriverRideHistory(driverId, new Callback<ResponseBody>() {

            @Override
            public void onResponse(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Response<ResponseBody> response
            ) {
                if (!isAdded() || getActivity() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RideDetailsDTO>>() {}.getType();
                        rideHistory = gson.fromJson(json, listType);
                        isDataLoaded = true;

                        openRideHistoryFragment();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(
                                requireContext(),
                                "Error parsing data",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    Toast.makeText(
                            requireContext(),
                            "Failed loading ride history",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<ResponseBody> call,
                    @NonNull Throwable t
            ) {
                if (!isAdded() || getActivity() == null) return;

                Toast.makeText(
                        requireContext(),
                        "Failed server communication",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void openRideHistoryFragment() {
        if (!isAdded() || getActivity() == null) return;

        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString(ARG_RIDE_HISTORY, gson.toJson(rideHistory));

        RideHistoryFragment fragment = new RideHistoryFragment();
        fragment.setArguments(args);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}