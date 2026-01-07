package com.example.taxiapp.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;

public class AdminRideDetailsFragment extends Fragment {

    private static final String ARG_RIDE_ID = "ride_id";
    private int rideId;

    public static AdminRideDetailsFragment newInstance(int rideId) {
        AdminRideDetailsFragment fragment = new AdminRideDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RIDE_ID, rideId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_details, container, false);

        if (getArguments() != null) {
            rideId = getArguments().getInt(ARG_RIDE_ID);
        }

        populateAdminRideData(view);

        return view;
    }

    private void populateAdminRideData(View view) {
        switch (rideId) {
            case 1:
                setRideData(view,
                        "Novi Sad → Beograd",
                        "Start: 01.12.2025 10:00\nEnd: 01.12.2025 11:15",
                        "Price: 2500 €",
                        "Status: Completed",
                        "No inconsistencies",
                        "4.5",
                        "Name: Marko Marković\nVehicle: Audi A3\nRating: 4.5",
                        "Ana Anić, Ivana Ilić",
                        "No",
                        false);
                break;

            case 2:
                setRideData(view,
                        "Beograd → Niš",
                        "Start: 03.12.2025 08:00\nEnd: 03.12.2025 08:30",
                        "Price: 0 €",
                        "Status: Cancelled\nCancelled by: DRIVER",
                        "Panic button triggered by passenger",
                        "N/A",
                        "Name: Jovan Jovanović\nVehicle: BMW 320\nRating: 4.0",
                        "Marko Petrović",
                        "Yes\nTriggered by: Passenger\nTime: 08:30",
                        true);
                break;

            case 3:
                setRideData(view,
                        "Subotica → Novi Sad",
                        "Start: 02.12.2025 12:00\nEnd: 02.12.2025 13:30",
                        "Price: 1800 €",
                        "Status: Completed",
                        "No inconsistencies",
                        "4.8",
                        "Name: Petar Petrović\nVehicle: VW Golf\nRating: 4.7",
                        "Milan Milanović, Ana Janković",
                        "No",
                        false);
                break;

            default:
                setRideData(view,
                        "Default Route",
                        "Start: 01.01.2025 00:00\nEnd: 01.01.2025 00:30",
                        "Price: 1000 €",
                        "Status: Completed",
                        "No inconsistencies",
                        "5.0",
                        "Name: Default Driver\nVehicle: Default Car\nRating: 5.0",
                        "Default Passenger",
                        "No",
                        false);
        }

        Button btnRepeatNow = view.findViewById(R.id.btnAdminRepeatNow);
        Button btnRepeatLater = view.findViewById(R.id.btnAdminRepeatLater);

        if (btnRepeatNow == null || btnRepeatLater == null) {

        } else {
            btnRepeatNow.setOnClickListener(v -> {

            });

            btnRepeatLater.setOnClickListener(v -> {

            });
        }
    }

    private void setRideData(View view,
                             String route,
                             String times,
                             String price,
                             String status,
                             String inconsistencies,
                             String rating,
                             String driver,
                             String passengers,
                             String panicInfo,
                             boolean showPanicSection) {


        // Start/End Time
        TextView tvStartEnd = view.findViewById(R.id.tvAdminDateTime);
        if (tvStartEnd != null) {
            tvStartEnd.setText(times);
        }

        // Price
        TextView tvPrice = view.findViewById(R.id.tvAdminPrice);
        if (tvPrice != null) {
            tvPrice.setText(price);
        }

        // Status
        TextView tvStatus = view.findViewById(R.id.tvAdminStatus);
        if (tvStatus != null) {
            tvStatus.setText(status);
        }

        // Inconsistencies
        TextView tvInconsistencies = view.findViewById(R.id.tvAdminInconsistencies);
        if (tvInconsistencies != null) {
            tvInconsistencies.setText(inconsistencies);
        }

        // Ratings
        TextView tvRating = view.findViewById(R.id.tvAdminRating);
        if (tvRating != null) {
            tvRating.setText("Passenger rating: " + rating);
        }

        // Driver
        TextView tvDriver = view.findViewById(R.id.tvAdminDriver);
        if (tvDriver != null) {
            tvDriver.setText(driver);
        }

        // Passengers
        TextView tvPassengers = view.findViewById(R.id.tvAdminPassengers);
        if (tvPassengers != null) {
            tvPassengers.setText(passengers);
        }

        // PANIC Info
        TextView tvPanic = view.findViewById(R.id.tvAdminPanic);
        if (tvPanic != null) {
            if (showPanicSection) {
                tvPanic.setText("PANIC Info:\n" + panicInfo);
                tvPanic.setVisibility(View.VISIBLE);
            } else {
                tvPanic.setVisibility(View.GONE);
            }
        }
    }
}