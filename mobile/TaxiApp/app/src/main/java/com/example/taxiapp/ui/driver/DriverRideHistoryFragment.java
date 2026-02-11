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
import com.example.taxiapp.ui.ride_history.RideHistoryFragment;
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

    private List<RideDetailsDTO> rideHistory = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_driver_ride_history,
                container,
                false
        );

        loadRideHistoryFromBackend();

        return view;
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
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RideDetailsDTO>>() {}.getType();
                        rideHistory = gson.fromJson(json, listType);

                        openRideHistoryFragment();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(
                                getContext(),
                                "Error parsing data",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    Toast.makeText(
                            getContext(),
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
                if (!isAdded()) return;

                Toast.makeText(
                        getContext(),
                        "Failed server communication",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void openRideHistoryFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container,
                        new RideHistoryFragment(rideHistory)
                )
                .commit();
    }
}
