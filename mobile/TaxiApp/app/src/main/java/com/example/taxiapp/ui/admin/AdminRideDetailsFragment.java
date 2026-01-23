package com.example.taxiapp.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.api.IMapController;

import model.RideDetailsDTO;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.api.ApiService;

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

        View view = inflater.inflate(R.layout.fragment_admin_ride_details, container, false);

        if (getArguments() != null) {
            rideId = getArguments().getInt(ARG_RIDE_ID);
            fetchRideDetails(rideId, view);
        }

        return view;
    }

    private void fetchRideDetails(long rideId, View view) {
        ApiService api = RetrofitClient.getApiService();
        api.getRideById(rideId).enqueue(new Callback<RideDetailsDTO>() {
            @Override
            public void onResponse(Call<RideDetailsDTO> call, Response<RideDetailsDTO> response) {
                if(response.isSuccessful() && response.body() != null) {
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
        ((TextView)view.findViewById(R.id.tvAdminRoute)).setText(ride.startLocation.address + " → " + ride.endLocation.address);
        ((TextView)view.findViewById(R.id.tvAdminRideInfo)).setText(
                "Start: " + ride.startTime + "\nEnd: " + ride.endTime +
                        "\nPrice: " + ride.price + " €\nStatus: " + ride.status
        );
        ((TextView)view.findViewById(R.id.tvAdminDriver)).setText(
                "Name: " + ride.driverName + "\nVehicle: " + ride.vehicle
        );
        ((TextView)view.findViewById(R.id.tvAdminPassengers)).setText(
                "Orderer: " + ride.ordererName + "\nLinked Passengers: " +
                        (ride.linkedPassengers != null && !ride.linkedPassengers.isEmpty() ?
                                String.join(", ", ride.linkedPassengers) : "None")
        );
        ((TextView)view.findViewById(R.id.tvAdminInconsistencies)).setText(
                ride.inconsistencies != null ? ride.inconsistencies : "No inconsistencies"
        );

        // PANIC
        TextView tvPanic = view.findViewById(R.id.tvAdminPanic);
        TextView tvPanicTitle = view.findViewById(R.id.tvAdminPanicTitle);
        if(ride.panic) {
            tvPanic.setText("PANIC Info:\n" + ride.panicTriggeredAt + " by " + ride.panicTriggeredBy);
            tvPanic.setVisibility(View.VISIBLE);
            tvPanicTitle.setVisibility(View.VISIBLE);
        } else {
            tvPanic.setVisibility(View.GONE);
            tvPanicTitle.setVisibility(View.GONE);
        }

        // Map
        setupMap(view, ride.startLocation.address, ride.endLocation.address);
}

    private void setupMap(View view, String start, String end) {
        MapView mapView = view.findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);
        IMapController mapController = mapView.getController();
        mapController.setZoom(12.0);

        // Geocoding placeholder - replace with actual geocoding
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
}
