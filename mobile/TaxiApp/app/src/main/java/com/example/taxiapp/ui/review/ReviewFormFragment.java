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

import model.CreateReviewRequest;
import model.ReviewDTO;
import model.RideDetailsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import service.AuthService;
import service.ReviewService;

public class ReviewFormFragment extends Fragment {

    private LinearLayout driverStarsLayout;
    private LinearLayout vehicleStarsLayout;
    private EditText commentEditText;
    private Button cancelButton, submitButton;

    private int driverRating = 0;
    private int vehicleRating = 0;
    private final int MAX_STARS = 5;

    private Long passengerId;
    private String passengerFullName;

    private RideDetailsDTO ride;

    public ReviewFormFragment() {
        // Required empty public constructor
    }

    public static ReviewFormFragment newInstance(RideDetailsDTO ride) {
        ReviewFormFragment fragment = new ReviewFormFragment();
        Bundle args = new Bundle();
        args.putSerializable("ride_key", ride);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ride = (RideDetailsDTO) getArguments().getSerializable("ride_key");
        }
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

        submitButton.setOnClickListener(v -> submitReview());

        passengerId = AuthService.getInstance().getLoggedInUserId(requireContext());
        passengerFullName = AuthService.getInstance().getUserFullName(requireContext());

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

    private void submitReview() {

        String comment = commentEditText.getText().toString().trim();

        if (driverRating == 0 || vehicleRating == 0) {
            Toast.makeText(getContext(), "Please rate both driver and vehicle", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateReviewRequest request = new CreateReviewRequest();
        request.rideId = ride.id;
        request.passengerId = passengerId;
        request.driverRating = driverRating;
        request.vehicleRating = vehicleRating;
        request.comment = comment;


        ReviewService reviewService = new ReviewService();

        reviewService.createReview(request, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    ReviewDTO reviewDTO = new ReviewDTO();
                    reviewDTO.rideId = ride.id;
                    reviewDTO.passengerId = passengerId;
                    reviewDTO.passengerName = passengerFullName;
                    reviewDTO.driverName = ride.driverName;
                    reviewDTO.driverRating = driverRating;
                    reviewDTO.vehicleRating = vehicleRating;
                    reviewDTO.comment = comment;

                    Bundle result = new Bundle();
                    result.putSerializable("newReview", reviewDTO);

                    getParentFragmentManager()
                            .setFragmentResult("reviewRequestKey", result);

                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error creating review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
