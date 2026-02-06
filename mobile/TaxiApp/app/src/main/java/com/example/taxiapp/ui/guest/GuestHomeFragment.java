package com.example.taxiapp.ui.guest;

import android.os.Bundle;
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

public class GuestHomeFragment extends Fragment {

    private List<ActiveVehicleLocationResponse> vehicleLocations = createMockVehicles();

    public GuestHomeFragment() {
    }

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
                    .replace(R.id.map_container, new MapFragment(vehicleLocations))
                    .commit();
        }
    }

    private List<ActiveVehicleLocationResponse> createMockVehicles() {
        List<ActiveVehicleLocationResponse> list = new ArrayList<>();

        ActiveVehicleLocationResponse v1 = new ActiveVehicleLocationResponse();
        v1.latitude = 45.2521;
        v1.longitude = 19.8358;
        v1.available = true;

        ActiveVehicleLocationResponse v2 = new ActiveVehicleLocationResponse();
        v2.latitude = 45.2509;
        v2.longitude = 19.8382;
        v2.available = false;

        list.add(v1);
        list.add(v2);

        return list;
    }

}