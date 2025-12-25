package com.example.taxiapp.ui.register;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;

public class RegisterFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etFirstName, etLastName, etEmail, etPassword, etConfirmPassword, etAddress, etPhone;
    private Button btnRegister, btnReset;
    private ImageView ivProfilePic;
    private Uri selectedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ivProfilePic.setImageURI(selectedImageUri);
        }
    }

    private void attemptRegister() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showMessage("Please fill all required fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match!");
            return;
        }

        showMessage("Registration successful! Activation email sent to " + email);
        resetForm();
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
        Toast.makeText(getActivity(), "Form reset", Toast.LENGTH_SHORT).show();
    }
}
