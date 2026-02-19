package com.example.taxiapp.ui.register_driver_vehicle_info;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taxiapp.R;

import model.RegisterDriverRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.DriverService;

public class RegisterDriverVehicleInfoFragment extends Fragment {


    private EditText etVehicleBrand, etVehicleModel, etPlateNumber, etNumberOfSeats;
    private AutoCompleteTextView spinnerVehicleType;
    private RadioGroup rgBaby, rgPets;
    private RadioButton rbBabyYes, rbPetsYes;

    public RegisterDriverVehicleInfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_driver_vehicle_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etVehicleBrand = view.findViewById(R.id.etVehicleBrand);
        etVehicleModel = view.findViewById(R.id.etVehicleModel);
        etPlateNumber = view.findViewById(R.id.etPlateNumber);
        etNumberOfSeats = view.findViewById(R.id.etNumberOfSeats);
        spinnerVehicleType = view.findViewById(R.id.spinnerVehicleType);

        rgBaby = view.findViewById(R.id.rgBaby);
        rgPets = view.findViewById(R.id.rgPets);
        rbBabyYes = view.findViewById(R.id.rbBabyYes);
        rbPetsYes = view.findViewById(R.id.rbPetsYes);

        Button btnRegister = view.findViewById(R.id.btnRegister);
        Button btnBack = view.findViewById(R.id.btnCancel);

        String[] types = {"STANDARD", "LUXURY", "VAN"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, types);
        spinnerVehicleType.setAdapter(adapter);

        btnRegister.setOnClickListener(v -> {
            if (validateVehicleInfo()) {
                performRegistration();
            }
        });

        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private boolean validateVehicleInfo() {
        boolean isValid = true;

        if (etVehicleBrand.getText().toString().trim().isEmpty()) {
            etVehicleBrand.setError("Brand is required");
            isValid = false;
        }
        if (etVehicleModel.getText().toString().trim().isEmpty()) {
            etVehicleModel.setError("Model is required");
            isValid = false;
        }
        if (etPlateNumber.getText().toString().trim().isEmpty()) {
            etPlateNumber.setError("Plate number is required");
            isValid = false;
        }

        String seatsStr = etNumberOfSeats.getText().toString().trim();
        if (seatsStr.isEmpty()) {
            etNumberOfSeats.setError("Required");
            isValid = false;
        } else if (Integer.parseInt(seatsStr) < 1) {
            etNumberOfSeats.setError("Min 1 seat");
            isValid = false;
        }

        if (spinnerVehicleType.getText().toString().isEmpty()) {
            spinnerVehicleType.setError("Type required");
            isValid = false;
        }

        return isValid;
    }

    private void performRegistration() {
        Bundle args = getArguments();
        if (args == null) {
            Toast.makeText(getContext(), "Error: Missing data from step 1", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterDriverRequest request = new RegisterDriverRequest();


        request.setFirstName(args.getString("firstName"));
        request.setLastName(args.getString("lastName"));
        request.setEmail(args.getString("email"));
        request.setAddress(args.getString("address"));
        request.setPhoneNumber(args.getString("phone"));

        request.setProfileImage(null);


        request.setBrand(etVehicleBrand.getText().toString().trim());
        request.setVehicleModel(etVehicleModel.getText().toString().trim());
        request.setPlateNum(etPlateNumber.getText().toString().trim());


        try {
            int seats = Integer.parseInt(etNumberOfSeats.getText().toString().trim());
            request.setSeatNum(seats);
        } catch (NumberFormatException e) {
            request.setSeatNum(4);
        }

        String vType = spinnerVehicleType.getText().toString().toUpperCase();
        request.setVehicleType(vType);

        request.setBabySafe(rbBabyYes.isChecked());
        request.setPetSafe(rbPetsYes.isChecked());


        DriverService.getInstance().registerDriver(request, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Registration successful! Activation email sent.", Toast.LENGTH_LONG).show();

                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                } else {
                    Log.e("API_ERROR", "Code: " + response.code());
                    Toast.makeText(getContext(), "Registration failed. Email might exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("NETWORK_ERROR", t.getMessage());
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}