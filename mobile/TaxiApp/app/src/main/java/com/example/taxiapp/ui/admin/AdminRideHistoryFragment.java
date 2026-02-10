package com.example.taxiapp.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.shared.RideHistoryFragment;

import service.AuthService;

public class AdminRideHistoryFragment extends Fragment {

    private boolean isFragmentCreated = false;

    public AdminRideHistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFragmentCreated = true;

        if (savedInstanceState != null) {
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
                R.layout.fragment_ride_history,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Long adminId = AuthService.getInstance().getLoggedInUserId(requireContext());

        if (adminId != -1) {
            RideHistoryFragment fragment = RideHistoryFragment.newInstanceForAdmin(adminId);

            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragmentCreated = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null && !getActivity().isChangingConfigurations()) {
        }
    }
}