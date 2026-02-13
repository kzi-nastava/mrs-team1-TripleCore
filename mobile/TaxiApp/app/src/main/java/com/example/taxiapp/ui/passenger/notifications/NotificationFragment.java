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

import com.example.taxiapp.R;
import model.NotificationResponse;


public class NotificationFragment extends Fragment {

    private static final String ARG_NOTIFICATION = "notification";

    private NotificationResponse notification;

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
        }

        return view;
    }

    private void handleNotificationLink(NotificationResponse notification) {
        if (notification.link == null || notification.link.isEmpty()) return;

        String[] parts = notification.link.split("[:]");
        if (parts.length != 2) return;

        String type = parts[0];      // review ili ride-tracking
        Long rideId;
        try {
            rideId = Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            return;
        }

        // Pozovi backend i otvori fragment
        fetchRideAndOpenFragment(type, rideId);
    }



}