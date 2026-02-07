package com.example.taxiapp.ui.passenger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.api.IMapController;

import java.util.List;

import model.RideDetailsDTO;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.api.ApiService;

public class  PassengerRideDetailsFragment extends Fragment {

    private static final String ARG_RIDE_ID = "ride_id";
    private int rideId;

    private Button btnRepeatNow, btnRepeatLater;

    public static PassengerRideDetailsFragment newInstance(int rideId) {
        PassengerRideDetailsFragment fragment = new PassengerRideDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RIDE_ID, rideId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_passenger_ride_details, container, false);

        btnRepeatNow = view.findViewById(R.id.btnRepeatNow);
        btnRepeatLater = view.findViewById(R.id.btnRepeatLater);

        if (getArguments() != null) {
            rideId = getArguments().getInt(ARG_RIDE_ID);
            fetchRideDetails(rideId, view);
        }

        setupButtonActions();

        return view;
    }

    private void fetchRideDetails(long rideId, View view) {
        ApiService api = RetrofitClient.getApiService();
        api.getRideById(rideId).enqueue(new Callback<RideDetailsDTO>() {
            @Override
            public void onResponse(Call<RideDetailsDTO> call, Response<RideDetailsDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateRideData(view, response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load ride details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RideDetailsDTO> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Failed to load ride details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateRideData(View view, RideDetailsDTO ride) {
        // Route
        ((TextView) view.findViewById(R.id.tvPassengerRoute))
                .setText(ride.startLocation.address + " → " + ride.endLocation.address);

        // Ride info
        ((TextView) view.findViewById(R.id.tvPassengerRideInfo))
                .setText("Start: " + ride.startTime + "\nEnd: " + ride.endTime +
                        "\nPrice: " + ride.price + " €\nStatus: " + ride.status);

        // Driver
        ((TextView) view.findViewById(R.id.tvPassengerDriver))
                .setText("Name: " + ride.driverName + "\nVehicle: " + ride.vehicle);

        // Passengers
        String passengersText = "Orderer: " + ride.ordererName + "\nLinked Passengers: " +
                (ride.linkedPassengers != null && !ride.linkedPassengers.isEmpty() ?
                        String.join(", ", ride.linkedPassengers) : "None");
        ((TextView) view.findViewById(R.id.tvPassengerPassengers)).setText(passengersText);

        // Inconsistencies
        ((TextView) view.findViewById(R.id.tvPassengerInconsistencies))
                .setText(ride.inconsistencies != null ? ride.inconsistencies : "No inconsistencies");

        // Map
        setupMap(view, ride.startLocation.address, ride.endLocation.address);
    }

    private void setupMap(View view, String start, String end) {
        MapView mapView = view.findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);
        IMapController mapController = mapView.getController();
        mapController.setZoom(12.0);

        GeoPoint startPoint = new GeoPoint(45.2671, 19.8335); // Novi Sad
        GeoPoint endPoint = new GeoPoint(44.8176, 20.4569);   // Beograd

        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(startPoint);
        startMarker.setTitle("Start");
        mapView.getOverlays().add(startMarker);

        Marker endMarker = new Marker(mapView);
        endMarker.setPosition(endPoint);
        endMarker.setTitle("End");
        mapView.getOverlays().add(endMarker);
    }

    private void setupButtonActions() {
        btnRepeatNow.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Repeat Now clicked", Toast.LENGTH_SHORT).show();

        });

        btnRepeatLater.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Repeat Later clicked", Toast.LENGTH_SHORT).show();

        });
    }
}
