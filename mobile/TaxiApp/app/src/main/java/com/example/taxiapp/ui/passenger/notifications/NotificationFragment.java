package com.example.taxiapp.ui.passenger.notifications;

import static helper.DateTimeHelper.getDateTime;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.review.ReviewFormFragment;
import com.example.taxiapp.ui.ride_tracking.RideTrackingFragment;

import model.NotificationResponse;
import model.RideDetailsDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.RideService;


public class NotificationFragment extends Fragment {

    private static final String ARG_NOTIFICATION = "notification";

    private NotificationResponse notification;
    private String linkType;
    private Long linkRideId;

    public static NotificationFragment newInstance(NotificationResponse notification) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTIFICATION, notification);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            notification = (NotificationResponse) getArguments().getSerializable(ARG_NOTIFICATION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        TextView tvTime = view.findViewById(R.id.tvTime);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        Button btnLink = view.findViewById(R.id.btn_notification_link);

        if (notification != null) {
            tvTitle.setText(notification.title);
            tvMessage.setText(notification.message);
            tvTime.setText(getDateTime(notification.time));

            if (handleNotificationLink(notification)){
                btnLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fetchRideAndOpenFragment();
                    }
                });
                btnLink.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    private boolean handleNotificationLink(NotificationResponse notification) {
        if (notification.link == null || notification.link.isEmpty()) return false;

        String[] parts = notification.link.split("[:]");
        if (parts.length != 2) return false;

        String type = parts[0];
        if (!type.equals("review") && !type.equals("ride-tracking")){
            return false;
        }
        linkType = type;

        try {
            linkRideId = Long.parseLong(parts[1]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    public void fetchRideAndOpenFragment(){
        RideService.getInstance().getRideDetails(linkRideId, new Callback<RideDetailsDTO>() {
            @Override
            public void onResponse(Call<RideDetailsDTO> call, Response<RideDetailsDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RideDetailsDTO ride = response.body();
                    if (linkType.equals("review")){
                        ReviewFormFragment reviewFormFragment = ReviewFormFragment.newInstance(ride);
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_container, reviewFormFragment)
                                .addToBackStack(null)
                                .commit();

                    } else if (linkType.equals("ride-tracking")) {
                        RideTrackingFragment rideTrackingFragment = RideTrackingFragment.newInstance(ride);
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_container, rideTrackingFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<RideDetailsDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load ride details", Toast.LENGTH_SHORT).show();
            }
        });

    }


}