package com.example.taxiapp.ui.auth.register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.taxiapp.ui.auth.login.LoginFragment;

import model.RegisterRequest;
import model.RegisterResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.AuthService;

public class RegisterFragment extends Fragment {

    private EditText etFirstName, etLastName, etEmail,
            etPassword, etConfirmPassword, etAddress, etPhone;
    private Button btnRegister, btnReset;
    private ImageView ivProfilePic;
    private Uri selectedImageUri;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK
                                && result.getData() != null
                                && result.getData().getData() != null) {

                            selectedImageUri = result.getData().getData();
                            ivProfilePic.setImageURI(selectedImageUri);
                        }
                    }
            );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etAddress = view.findViewById(R.id.etAddress);
        etPhone = view.findViewById(R.id.etPhone);

        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnReset = view.findViewById(R.id.btnReset);

        ivProfilePic.setOnClickListener(v -> openImagePicker());
        btnRegister.setOnClickListener(v -> attemptRegister());
        btnReset.setOnClickListener(v -> resetForm());

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void attemptRegister() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()
                || address.isEmpty() || phone.isEmpty()) {
            showMessage("Please fill all required fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match!");
            return;
        }

        RegisterRequest request = new RegisterRequest(
                firstName,
                lastName,
                email,
                password,
                confirmPassword,
                address,
                phone,
                null,          // profileImage (za sad)
                "PASSENGER"    // role
        );

        AuthService.getInstance().register(request, new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call,
                                   Response<RegisterResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    showMessage(response.body().getMessage());

                    // redirect to login
                    if (getActivity() instanceof MainActivity){
                        MainActivity main = (MainActivity) getActivity();
                        main.loadFragment(new LoginFragment(), true);
                    }
                } else {
                    showMessage("Registration failed. Email may already exist.");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                showMessage("Server error. Please try again later.");
            }
        });
    }

    private void showMessage(String message) {
        new AlertDialog.Builder(requireActivity())
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void resetForm() {
        etFirstName.setText("");
        etLastName.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
        etAddress.setText("");
        etPhone.setText("");
        ivProfilePic.setImageResource(R.drawable.profile);
        selectedImageUri = null;
    }
}