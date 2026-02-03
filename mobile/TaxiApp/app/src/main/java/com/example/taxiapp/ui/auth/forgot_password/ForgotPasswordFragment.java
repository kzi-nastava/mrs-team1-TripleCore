package com.example.taxiapp.ui.auth.forgot_password;

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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.AuthService;

public class ForgotPasswordFragment extends Fragment {

    private EditText etEmail;
    private Button btnSendLink;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        etEmail = view.findViewById(R.id.etEmail);
        btnSendLink = view.findViewById(R.id.btnSendReset);
        TextView tvBackToLogin = view.findViewById(R.id.tvBackToLogin);

        btnSendLink.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(
                        getActivity(),
                        "Please enter your email",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            btnSendLink.setEnabled(false);

            AuthService.getInstance().forgotPassword(email, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    btnSendLink.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseText = response.body().string();
                            Toast.makeText(
                                    getActivity(),
                                    responseText,
                                    Toast.LENGTH_LONG
                            ).show();
                        } catch (Exception e) {
                            Toast.makeText(
                                    getActivity(),
                                    "Email sent successfully!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    } else {
                        try {
                            String errorText = response.errorBody() != null ?
                                    response.errorBody().string() : "Unknown error";
                            Toast.makeText(
                                    getActivity(),
                                    "Error: " + errorText,
                                    Toast.LENGTH_LONG
                            ).show();
                        } catch (Exception e) {
                            Toast.makeText(
                                    getActivity(),
                                    "Something went wrong",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    btnSendLink.setEnabled(true);
                    Toast.makeText(
                            getActivity(),
                            "Network error: " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });

        tvBackToLogin.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        return view;
    }
}
