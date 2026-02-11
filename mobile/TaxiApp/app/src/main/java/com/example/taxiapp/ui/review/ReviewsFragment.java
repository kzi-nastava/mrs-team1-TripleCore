package com.example.taxiapp.ui.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;
import model.ReviewDTO;
import service.AuthService;
import service.ReviewService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsFragment extends Fragment {

    private RecyclerView rvReviews;
    private TextView tvNoReviews;

    private List<ReviewDTO> reviews = new ArrayList<>();
    private ReviewsAdapter adapter;

    public ReviewsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        rvReviews = view.findViewById(R.id.rvReviews);
        tvNoReviews = view.findViewById(R.id.tvNoReviews);

        rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ReviewsAdapter(reviews);
        rvReviews.setAdapter(adapter);

        fetchReviews();

        return view;
    }

    private void fetchReviews() {
        Long userId = AuthService.getInstance().getLoggedInUserId(requireContext());
        String role = AuthService.getInstance().getLoggedInUserRole(requireContext());
        ReviewService reviewService = new ReviewService();

        Callback<List<ReviewDTO>> callback = new Callback<List<ReviewDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewDTO>> call, Response<List<ReviewDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviews.clear();
                    reviews.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    tvNoReviews.setVisibility(reviews.isEmpty() ? View.VISIBLE : View.GONE);
                    rvReviews.setVisibility(reviews.isEmpty() ? View.GONE : View.VISIBLE);
                } else {
                    tvNoReviews.setVisibility(View.VISIBLE);
                    rvReviews.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ReviewDTO>> call, Throwable t) {
                tvNoReviews.setText("Failed to load reviews");
                tvNoReviews.setVisibility(View.VISIBLE);
                rvReviews.setVisibility(View.GONE);
            }
        };

        if ("PASSENGER".equals(role)) {
            reviewService.getPassengerReviews(userId, callback);
        } else if ("DRIVER".equals(role)) {
            reviewService.getDriverReviews(userId, callback);
        } else {
            tvNoReviews.setText("No reviews available for your role");
            tvNoReviews.setVisibility(View.VISIBLE);
        }
    }

    private static class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

        private final List<ReviewDTO> reviewList;

        public ReviewsAdapter(List<ReviewDTO> reviewList) {
            this.reviewList = reviewList;
        }

        @NonNull
        @Override
        public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_review, parent, false);
            return new ReviewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
            ReviewDTO review = reviewList.get(position);

            holder.tvPassengerName.setText(review.passengerName);
            holder.tvDriverName.setText(review.driverName);

            // Ocene - prikaz zvezdica za vozača
            holder.llDriverStars.removeAllViews();
            for (int i = 0; i < 5; i++) {
                ImageView star = new ImageView(holder.itemView.getContext());
                star.setImageResource(i < review.driverRating
                        ? R.drawable.star_full
                        : R.drawable.star_empty);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(2, 0, 2, 0);
                star.setLayoutParams(params);
                holder.llDriverStars.addView(star);
            }

            // Ocene - prikaz zvezdica za vozilo
            holder.llVehicleStars.removeAllViews();
            for (int i = 0; i < 5; i++) {
                ImageView star = new ImageView(holder.itemView.getContext());
                star.setImageResource(i < review.vehicleRating
                        ? R.drawable.star_full
                        : R.drawable.star_empty);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(2, 0, 2, 0);
                star.setLayoutParams(params);
                holder.llVehicleStars.addView(star);
            }

            // Komentar
            holder.tvComment.setText(review.comment != null ? review.comment : "");
        }

        @Override
        public int getItemCount() {
            return reviewList.size();
        }

        static class ReviewViewHolder extends RecyclerView.ViewHolder {
            TextView tvPassengerName, tvDriverName, tvComment;
            LinearLayout llDriverStars, llVehicleStars;

            public ReviewViewHolder(@NonNull View itemView) {
                super(itemView);
                tvPassengerName = itemView.findViewById(R.id.tvPassengerName);
                tvDriverName = itemView.findViewById(R.id.tvDriverName);
                tvComment = itemView.findViewById(R.id.tvComment);
                llDriverStars = itemView.findViewById(R.id.llDriverStars);
                llVehicleStars = itemView.findViewById(R.id.llVehicleStars);
            }
        }
    }
}
