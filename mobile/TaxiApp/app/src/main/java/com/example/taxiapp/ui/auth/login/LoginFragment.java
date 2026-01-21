package com.example.taxiapp.ui.auth.login;

import android.content.Context;
import android.content.SharedPreferences;
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

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

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
            Toast.makeText(getActivity(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthService.getInstance().login(email, password, new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    SharedPreferences prefs = requireActivity()
                            .getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    prefs.edit()
                            .putBoolean("isLoggedIn", true)
                            .putString("userType", loginResponse.getRole().name())
                            .putString("userEmail", loginResponse.getEmail())
                            .putString("userFirstName", loginResponse.getFirstName())
                            .putString("userLastName", loginResponse.getLastName())
                            .apply();

                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(),
                                loginResponse.getRole() + " login successful!",
                                Toast.LENGTH_SHORT).show();

                        if (getActivity() instanceof MainActivity) {
                            MainActivity main = (MainActivity) getActivity();
                            if (UserRole.ADMIN.equals(loginResponse.getRole())) {
                                main.onAdminLoginSuccess();
                            } else if (UserRole.DRIVER.equals(loginResponse.getRole())) {
                                main.onDriverLoginSuccess();
                            }
                            // here is going to be PASSENGER
                        }
                    });

                    clearFields();

                } else {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                        clearFields();
                    });
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (!isAdded()) return;
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Login failed: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void clearFields() {
        etEmail.setText("");
        etPassword.setText("");
    }
}
