package com.example.taxiapp.ui.driver_additional_info;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.profile_info.ProfileFragment;

import service.ProfileService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverAdditionalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverAdditionalInfoFragment extends Fragment {


    private TextView tvWorkingHours;

    private TextView tvVehicleBrand;
    private TextView tvVehicleModel;
    private TextView tvVehicleType;
    private TextView tvLicencePlate;
    private TextView tvSeatNumber;
    private TextView tvBabyTransport;
    private TextView tvPetTransport;

    private ProfileService profileService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverAdditionalInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment driverAdditionalInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverAdditionalInfoFragment newInstance(String param1, String param2) {
        DriverAdditionalInfoFragment fragment = new DriverAdditionalInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_driver_additional_info, container, false);

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

        tvWorkingHours = view.findViewById(R.id.tvWorkingHours);

        tvVehicleBrand = view.findViewById(R.id.tvVehicleBrand);
        tvVehicleModel = view.findViewById(R.id.tvVehicleModel);
        tvVehicleType = view.findViewById(R.id.tvVehicleType);
        tvLicencePlate = view.findViewById(R.id.tvLicencePlate);
        tvSeatNumber = view.findViewById(R.id.tvSeatNumber);
        tvBabyTransport = view.findViewById(R.id.tvBabyTransport);
        tvPetTransport = view.findViewById(R.id.tvPetTransport);

        profileService = ProfileService.getInstance();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", getActivity().MODE_PRIVATE);
        Long userId = sharedPreferences.getLong("userId", -1);

        if (userId != -1) {
            loadDriverData(userId);
        }else {
            Log.e("DriverFragment", "No driver ID found in SharedPreferences");
        }




        return view;

    }

    private void loadDriverData(Long driverId) {
        profileService.getDriverProfile(driverId, new retrofit2.Callback<model.DriverProfileResponse>() {
            @Override
            public void onResponse(retrofit2.Call<model.DriverProfileResponse> call, retrofit2.Response<model.DriverProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    model.DriverProfileResponse driver = response.body();


                    tvWorkingHours.setText("Working hours in last 24 hours: " + driver.getWorkingHoursToday() + " h");

                    tvVehicleBrand.setText("Vehicle brand: " + driver.getVehicle().getBrand());
                    tvVehicleModel.setText("Vehicle model: " + driver.getVehicle().getModel());
                    tvVehicleType.setText("Vehicle type: " + driver.getVehicle().getType().toString());
                    tvLicencePlate.setText("Licence plate number: " + driver.getVehicle().getPlateNumber());
                    tvSeatNumber.setText("Number of seats: " + String.valueOf(driver.getVehicle().getSeatNumber()));
                    tvBabyTransport.setText("Is vehicle baby friendly: " + (driver.getVehicle().isBabyFriendly() ? "Yes" : "No"));
                    tvPetTransport.setText("Is vehicle pet friendly: " + (driver.getVehicle().isPetFriendly() ? "Yes" : "No"));
                }
            }

            @Override
            public void onFailure(retrofit2.Call<model.DriverProfileResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}