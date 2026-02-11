package com.example.taxiapp.ui.guest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.map.MapFragment;

import java.util.ArrayList;
import java.util.List;

import model.ActiveVehicleLocationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.VehicleService;

public class GuestHomeFragment extends Fragment {

    private List<ActiveVehicleLocationResponse> vehicleLocations = new ArrayList<>();
    MapFragment mapFragment = new MapFragment();

    private static final long POLLING_INTERVAL = 3000; // 3 seconds
    private final Handler handler = new Handler(Looper.getMainLooper());


    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_guest_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        handler.post(pollingRunnable);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(pollingRunnable);
    }

    private final Runnable pollingRunnable = new Runnable() {
        @Override
        public void run() {
            fetchVehicleLocations();
            handler.postDelayed(this, POLLING_INTERVAL);
        }
    };

    private void fetchVehicleLocations() {
        VehicleService.getInstance()
                .getVehicleLocations(null, new Callback<List<ActiveVehicleLocationResponse>>() {

                    @Override
                    public void onResponse(Call<List<ActiveVehicleLocationResponse>> call,
                                           Response<List<ActiveVehicleLocationResponse>> response) {

                        Log.d("API", "CODE: " + response.code());

                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {

                            Log.d("API", "BODY SIZE: " + response.body().size());

                            vehicleLocations.clear();
                            vehicleLocations.addAll(response.body());

                            if (mapFragment != null) {
                                mapFragment.updateVehicleLocations(vehicleLocations);
                            }

                        } else {
                            Log.e("API", "ERROR BODY: " + response.errorBody());
                        }
                    }


                    @Override
                    public void onFailure(Call<List<ActiveVehicleLocationResponse>> call, Throwable t) {
                        if (!isAdded()) return;
                        Log.e("GuestHomeFragment", "Failed to fetch vehicle locations", t);
                    }
                });
    }



//    private List<ActiveVehicleLocationResponse> createMockVehicles() {
//        List<ActiveVehicleLocationResponse> list = new ArrayList<>();
//
//        ActiveVehicleLocationResponse v1 = new ActiveVehicleLocationResponse();
//        v1.latitude = 45.2521;
//        v1.longitude = 19.8358;
//        v1.available = true;
//
//        ActiveVehicleLocationResponse v2 = new ActiveVehicleLocationResponse();
//        v2.latitude = 45.2509;
//        v2.longitude = 19.8382;
//        v2.available = false;
//
//        list.add(v1);
//        list.add(v2);
//
//        return list;
//    }

}