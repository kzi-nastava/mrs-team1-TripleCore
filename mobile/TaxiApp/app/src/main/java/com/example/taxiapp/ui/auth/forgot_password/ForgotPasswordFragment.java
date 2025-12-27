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
                Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(
                        getActivity(),
                        "Reset link sent to " + email,
                        Toast.LENGTH_LONG
                ).show();

                etEmail.setText("");
            }
        });

        tvBackToLogin.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .popBackStack();
        });

        return view;
    }
}
