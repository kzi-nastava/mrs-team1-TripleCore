package com.example.taxiapp.ui.auth.reset_password;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;

public class ResetPasswordFragment extends Fragment {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnResetPassword;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        etNewPassword = view.findViewById(R.id.etNewPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnResetPassword = view.findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(v -> resetPassword());

        return view;
    }

    private void resetPassword() {
        String password = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
            clearForm();
            return;
        }

        Toast.makeText(getContext(), "Password successfully reset!", Toast.LENGTH_SHORT).show();
        clearForm();
    }

    private void clearForm() {
        etNewPassword.setText("");
        etConfirmPassword.setText("");
    }
}
