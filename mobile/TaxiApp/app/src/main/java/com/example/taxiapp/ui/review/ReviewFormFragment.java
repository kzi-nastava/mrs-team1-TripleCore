package com.example.taxiapp.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;

public class ReviewFormFragment extends Fragment {

    private LinearLayout driverStarsLayout;
    private LinearLayout vehicleStarsLayout;
    private EditText commentEditText;
    private Button cancelButton, submitButton;

    private int driverRating = 0;
    private int vehicleRating = 0;
    private final int MAX_STARS = 5;

    public ReviewFormFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_form, container, false);

        driverStarsLayout = view.findViewById(R.id.driverStars);
        vehicleStarsLayout = view.findViewById(R.id.vehicleStars);
        commentEditText = view.findViewById(R.id.commentEditText);
        cancelButton = view.findViewById(R.id.cancelButton);
        submitButton = view.findViewById(R.id.submitButton);

        setupStars(driverStarsLayout, true);
        setupStars(vehicleStarsLayout, false);

        cancelButton.setOnClickListener(v -> {
            // Očisti formu ili zatvori fragment
            driverRating = 0;
            vehicleRating = 0;
            commentEditText.setText("");
            updateStars(driverStarsLayout, driverRating);
            updateStars(vehicleStarsLayout, vehicleRating);
            Toast.makeText(getContext(), "Review canceled", Toast.LENGTH_SHORT).show();
        });

        submitButton.setOnClickListener(v -> {
            if (driverRating == 0 || vehicleRating == 0) {
                Toast.makeText(getContext(), "Please rate both driver and vehicle", Toast.LENGTH_SHORT).show();
                return;
            }
            String comment = commentEditText.getText().toString().trim();
            // Ovde pozovi API ili sačuvaj podatke
            Toast.makeText(getContext(), "Submitted! Driver: " + driverRating +
                    " Vehicle: " + vehicleRating + "\nComment: " + comment, Toast.LENGTH_LONG).show();
        });

        return view;
    }

    private void setupStars(LinearLayout layout, boolean isDriver) {
        layout.removeAllViews();
        for (int i = 0; i < MAX_STARS; i++) {
            final int index = i;
            ImageView star = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            star.setLayoutParams(params);
            star.setImageResource(R.drawable.star_empty);

            star.setOnClickListener(v -> {
                if (isDriver) {
                    driverRating = index + 1;
                    updateStars(driverStarsLayout, driverRating);
                } else {
                    vehicleRating = index + 1;
                    updateStars(vehicleStarsLayout, vehicleRating);
                }
            });

            layout.addView(star);
        }
    }

    private void updateStars(LinearLayout layout, int rating) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            ImageView star = (ImageView) layout.getChildAt(i);
            if (i < rating) {
                star.setImageResource(R.drawable.star_full);
            } else {
                star.setImageResource(R.drawable.star_empty);
            }
        }
    }
}
