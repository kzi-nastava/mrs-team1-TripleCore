package com.example.taxiapp.ui.driver_additional_info;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.profile_info.ProfileFragment;

import java.util.Map;

import model.UserBlockedResponse;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.ProfileService;

public class DriverAdditionalInfoFragment extends Fragment {

    private TextView tvWorkingHours, tvVehicleBrand, tvVehicleModel, tvVehicleType;
    private TextView tvLicencePlate, tvSeatNumber, tvBabyTransport, tvPetTransport;


    private View layoutDriverBlocked;
    private TextView tvDriverBlockedNote;

    private ProfileService profileService;
    private static final String TAG = "DriverAdditionalInfo";

    public DriverAdditionalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_additional_info, container, false);

        initViews(view);

        Button btnGoToProfile = view.findViewById(R.id.btnChangeInfo);
        btnGoToProfile.setOnClickListener(v -> {
            Fragment fragment = new ProfileFragment();
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        profileService = ProfileService.getInstance();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        Long userId = sharedPreferences.getLong("userId", -1);

        if (userId != -1) {
            checkBlockStatus(userId);
            loadDriverProfileData(userId);
        } else {
            Log.e(TAG, "No driver ID found in SharedPreferences");
        }

        return view;
    }

    private void initViews(View view) {
        tvWorkingHours = view.findViewById(R.id.tvWorkingHours);
        tvVehicleBrand = view.findViewById(R.id.tvVehicleBrand);
        tvVehicleModel = view.findViewById(R.id.tvVehicleModel);
        tvVehicleType = view.findViewById(R.id.tvVehicleType);
        tvLicencePlate = view.findViewById(R.id.tvLicencePlate);
        tvSeatNumber = view.findViewById(R.id.tvSeatNumber);
        tvBabyTransport = view.findViewById(R.id.tvBabyTransport);
        tvPetTransport = view.findViewById(R.id.tvPetTransport);

        layoutDriverBlocked = view.findViewById(R.id.layoutDriverBlocked);
        tvDriverBlockedNote = view.findViewById(R.id.tvDriverBlockedNote);
    }


    private void checkBlockStatus(Long userId) {
        RetrofitClient.getApiService().getUserById(userId).enqueue(new Callback<UserBlockedResponse>() {
            @Override
            public void onResponse(Call<UserBlockedResponse> call, Response<UserBlockedResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    if (response.body().isBlocked()) {

                        loadBlockedNote(userId);
                    } else {
                        layoutDriverBlocked.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserBlockedResponse> call, Throwable t) {
                Log.e(TAG, "Block status check failed: " + t.getMessage());
            }
        });
    }


    private void loadBlockedNote(Long userId) {
        RetrofitClient.getApiService().getBlockedNote(userId).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (isAdded()) {
                    layoutDriverBlocked.setVisibility(View.VISIBLE);
                    if (response.isSuccessful() && response.body() != null) {
                        String note = response.body().get("note");
                        tvDriverBlockedNote.setText("Reason: " + (note != null && !note.isEmpty() ? note : "Not specified"));
                    } else {
                        tvDriverBlockedNote.setText("Reason: Account suspended by administrator.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                if (isAdded()) {
                    layoutDriverBlocked.setVisibility(View.VISIBLE);
                    tvDriverBlockedNote.setText("Reason: Blocked (Server error)");
                }
            }
        });
    }


    private void loadDriverProfileData(Long driverId) {
        profileService.getDriverProfile(driverId, new Callback<model.DriverProfileResponse>() {
            @Override
            public void onResponse(Call<model.DriverProfileResponse> call, Response<model.DriverProfileResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    model.DriverProfileResponse driver = response.body();

                    tvWorkingHours.setText("Working hours in last 24 hours: " + driver.getWorkingHoursToday() + " h");

                    if (driver.getVehicle() != null) {
                        tvVehicleBrand.setText("Vehicle brand: " + driver.getVehicle().getBrand());
                        tvVehicleModel.setText("Vehicle model: " + driver.getVehicle().getModel());
                        tvVehicleType.setText("Vehicle type: " + driver.getVehicle().getType().toString());
                        tvLicencePlate.setText("Licence plate number: " + driver.getVehicle().getPlateNumber());
                        tvSeatNumber.setText("Number of seats: " + driver.getVehicle().getSeatNumber());
                        tvBabyTransport.setText("Is vehicle baby friendly: " + (driver.getVehicle().isBabyFriendly() ? "Yes" : "No"));
                        tvPetTransport.setText("Is vehicle pet friendly: " + (driver.getVehicle().isPetFriendly() ? "Yes" : "No"));
                    }
                }
            }

            @Override
            public void onFailure(Call<model.DriverProfileResponse> call, Throwable t) {
                Log.e(TAG, "Profile data load failed: " + t.getMessage());
            }
        });
    }
}