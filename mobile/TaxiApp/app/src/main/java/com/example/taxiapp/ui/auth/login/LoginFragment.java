package com.example.taxiapp.ui.auth.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;

public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Initialize views
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        // Set click listener for login button
        btnLogin.setOnClickListener(v -> {
            attemptLogin();
        });

        TextView tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(v -> {
            // navigate to ForgotPasswordFragment
            tvForgotPassword.setOnClickListener(view1 -> {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, new com.example.taxiapp.ui.auth.forgot_password.ForgotPasswordFragment())
                        .addToBackStack(null)
                        .commit();
            });

        });

        return view;
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.equals("driver@example.com") && password.equals("driver123")) {
            Toast.makeText(getActivity(), "Login successful!", Toast.LENGTH_SHORT).show();

            if (getActivity() instanceof com.example.taxiapp.ui.MainActivity) {
                ((com.example.taxiapp.ui.MainActivity) getActivity()).onLoginSuccess();
            }

            clearFields();
        } else {
            Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_SHORT).show();
            clearFields();
        }

    }

    private void clearFields() {
        etEmail.setText("");
        etPassword.setText("");
    }
}