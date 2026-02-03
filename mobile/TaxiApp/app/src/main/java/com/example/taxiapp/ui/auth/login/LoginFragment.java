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
import com.example.taxiapp.ui.MainActivity;
import com.example.taxiapp.ui.auth.forgot_password.ForgotPasswordFragment;

import enums.UserRole;
import model.LoginResponse;
import service.AuthService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private AuthService authService;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        authService = AuthService.getInstance();

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> attemptLogin());

        TextView tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new ForgotPasswordFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter email and password");
            return;
        }

        btnLogin.setEnabled(false);

        authService.login(email, password, new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Login");

                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    // Save session
                    authService.saveLoginSession(requireContext(), loginResponse);

                    showToast(loginResponse.getRole() + " login successful!");

                    // Navigate based on role
                    navigateBasedOnRole(loginResponse);

                    clearFields();

                } else {
                    showToast("Invalid credentials");
                    clearFields();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Login");

                if (!isAdded()) return;
                showToast("Login failed: " + t.getMessage());
            }
        });
    }

    private void navigateBasedOnRole(LoginResponse loginResponse) {
        if (getActivity() instanceof MainActivity) {
            MainActivity main = (MainActivity) getActivity();
            UserRole role = loginResponse.getRole();

            if (UserRole.ADMIN.equals(role)) {
                main.onAdminLoginSuccess();
            } else if (UserRole.DRIVER.equals(role)) {
                main.onDriverLoginSuccess();
            } else if (UserRole.PASSENGER.equals(role)) {
                main.onPassengerLoginSuccess();
            }
        }
    }

    private void showToast(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void clearFields() {
        etEmail.setText("");
        etPassword.setText("");
    }
}