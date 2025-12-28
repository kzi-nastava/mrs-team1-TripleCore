package com.example.taxiapp.ui.driver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.MainActivity;
import com.example.taxiapp.ui.map.MapFragment;

public class DriverHomeFragment extends Fragment {
    private DriverHomeViewModel viewModel;

    public DriverHomeFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(DriverHomeViewModel.class);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_container);

        if (mapFragment == null) {
            mapFragment = new MapFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }

        Button btnStatus = view.findViewById(R.id.btn_go_inactive);

        viewModel.getIsActive().observe(getViewLifecycleOwner(), isActive -> {
            updateStatusButton(btnStatus, isActive);
        });

        btnStatus.setOnClickListener(v -> {
            viewModel.toggleActive();
            Boolean current = viewModel.getIsActive().getValue();
            Toast.makeText(getContext(),
                    current != null && current ? "You are now active" : "You are now inactive",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void updateStatusButton(Button button, boolean isActive) {
        if (isActive) {
            button.setText("Go Inactive");
            button.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.btn_active_red));
            button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white));

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity())
                        .setLogoutEnabled(false, "Logout (Must go inactive first)");
            }

        } else {
            button.setText("Go Active");
            button.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), android.R.color.white));
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.btn_active_red));
            button.setBackgroundResource(R.drawable.border_red_btn);

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity())
                        .setLogoutEnabled(true, "Logout");
            }
        }
    }
}
