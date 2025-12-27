package com.example.taxiapp.ui.driver;

import android.content.ClipData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.map.MapFragment;
import com.google.android.material.navigation.NavigationView;

public class DriverHomeFragment extends Fragment {
    private boolean isActive = true;
    private MenuItem logoutItem;

    public DriverHomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_driver_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, new MapFragment())
                    .commit();
        }

        Button btnStatus = view.findViewById(R.id.btn_go_inactive);

        NavigationView navView = requireActivity().findViewById(R.id.navigation_view);
        logoutItem = navView.getMenu().findItem(R.id.nav_logout);

        updateStatusButton(btnStatus);

        btnStatus.setOnClickListener(v -> {
            isActive = !isActive;
            updateStatusButton(btnStatus);

            Toast.makeText(getContext(),
                    isActive ? "You are now active" : "You are now inactive",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void updateStatusButton(Button button) {
        if (isActive) {
            button.setText("Go Inactive");
            button.setBackgroundTintList(getResources().getColorStateList(R.color.btn_active_red));
            button.setTextColor(getResources().getColor(android.R.color.white));

            logoutItem.setEnabled(false);
            logoutItem.setTitle("Logout (Must go inactive first)");
        } else {
            button.setText("Go Active");
            button.setBackgroundTintList(getResources().getColorStateList(android.R.color.white));
            button.setTextColor(getResources().getColor(R.color.btn_active_red));
            button.setBackgroundResource(R.drawable.border_red_btn);

            logoutItem.setEnabled(true);
            logoutItem.setTitle("Logout");
        }
    }

}
