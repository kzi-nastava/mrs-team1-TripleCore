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
    private Button btnStatus;

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

        if (!viewModel.initialize(requireContext())) {
            Toast.makeText(requireContext(), "Access denied - Driver only", Toast.LENGTH_SHORT).show();

            requireActivity().getOnBackPressedDispatcher().onBackPressed();
            return;
        }

        setupMapFragment();
        setupUI(view);
        setupObservers();
    }

    private void setupMapFragment() {
        MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_container);

        if (mapFragment == null) {
            mapFragment = new MapFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }
    }

    private void setupUI(View view) {
        btnStatus = view.findViewById(R.id.btn_go_inactive);

        btnStatus.setOnClickListener(v -> {
            btnStatus.setEnabled(false);
            btnStatus.setText("Updating...");

            viewModel.toggleActive();

        });
    }

    private void setupObservers() {
        viewModel.getIsActive().observe(getViewLifecycleOwner(), isActive -> {
            if (btnStatus != null) {
                updateStatusButton(isActive);
                btnStatus.setEnabled(true);
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateStatusButton(boolean isActive) {
        if (isActive) {
            btnStatus.setText("Go Inactive");
            btnStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), R.color.btn_active_red));
            btnStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), android.R.color.white));

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity())
                        .setLogoutEnabled(false, "Logout (Must go inactive first)");
            }

        } else {
            btnStatus.setText("Go Active");
            btnStatus.setBackgroundTintList(
                    ContextCompat.getColorStateList(requireContext(), android.R.color.white));
            btnStatus.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.btn_active_red));
            btnStatus.setBackgroundResource(R.drawable.border_red_btn);

            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity())
                        .setLogoutEnabled(true, "Logout");
            }
        }
    }
}