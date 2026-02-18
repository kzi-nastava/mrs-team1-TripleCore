package com.example.taxiapp.ui.register_driver_info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.MainActivity;
import com.example.taxiapp.ui.register_driver_vehicle_info.RegisterDriverVehicleInfoFragment;

public class RegisterDriverInfoFragment extends Fragment {

    private ImageView ivDriverProfilePic;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    public RegisterDriverInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_register_driver_info, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        ivDriverProfilePic.setImageURI(selectedImageUri);
                    }
                }
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivDriverProfilePic = view.findViewById(R.id.ivDriverProfilePic);
        ivDriverProfilePic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });


        EditText etFirstName = view.findViewById(R.id.etFirstName);
        EditText etLastName = view.findViewById(R.id.etLastName);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etAddress = view.findViewById(R.id.etAddress);
        EditText etPhone = view.findViewById(R.id.etPhone);



        Button btnNext = view.findViewById(R.id.btnNext);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnNext.setOnClickListener(v -> {
            boolean isValid = true;


            if (etFirstName.getText().toString().trim().isEmpty()) {
                etFirstName.setError("First name is required");
                isValid = false;
            }

            if (etLastName.getText().toString().trim().isEmpty()) {
                etLastName.setError("Last name is required");
                isValid = false;
            }


            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                isValid = false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Email must be valid");
                isValid = false;
            }


            if (etAddress.getText().toString().trim().isEmpty()) {
                etAddress.setError("Address is required");
                isValid = false;
            }


            String phone = etPhone.getText().toString().trim();
            String phoneRegex = "\\+?[0-9]{10,15}";
            if (phone.isEmpty()) {
                etPhone.setError("Phone number is required");
                isValid = false;
            } else if (!phone.matches(phoneRegex)) {
                etPhone.setError("Invalid phone number (must be 10-15 digits)");
                isValid = false;
            }


            if (isValid) {
                RegisterDriverVehicleInfoFragment vehicleFragment = new RegisterDriverVehicleInfoFragment();


                Bundle args = new Bundle();
                args.putString("firstName", etFirstName.getText().toString());
                args.putString("lastName", etLastName.getText().toString());
                args.putString("email", email);
                args.putString("address", etAddress.getText().toString());
                args.putString("phone", phone);

                if (selectedImageUri != null) {
                    args.putString("profileImageUri", selectedImageUri.toString());
                }

                vehicleFragment.setArguments(args);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main_container, vehicleFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }
}