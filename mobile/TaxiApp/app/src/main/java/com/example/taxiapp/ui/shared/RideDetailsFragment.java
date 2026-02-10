package com.example.taxiapp.ui.shared;

import static android.view.View.VISIBLE;
import static helper.DateTimeHelper.getTimeOnly;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import helper.RideActionHelper;
import com.google.gson.Gson;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import helper.RouteHelper;
import model.ReviewDTO;
import model.RideDetailsDTO;
import model.LocationDTO;
import service.AuthService;

public class RideDetailsFragment extends Fragment {

    private static final String ARG_RIDE_DETAILS = "ride_details";

    private RideDetailsDTO ride;
    private String currentUserRole; // "DRIVER", "PASSENGER", "ADMIN"
    private Long currentUserId;

    // UI elements
    private MapView mapFragment;
    private Button btnCancelRide;

    private double savedLat = Double.NaN;
    private double savedLon = Double.NaN;
    private double savedZoom = Double.NaN;

    public static RideDetailsFragment newInstance(RideDetailsDTO rideDetails, String userRole) {
        RideDetailsFragment fragment = new RideDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RIDE_DETAILS, new Gson().toJson(rideDetails));
        fragment.setArguments(args);
        return fragment;
    }

    public RideDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String json = getArguments().getString(ARG_RIDE_DETAILS);
            if (json != null) {
                Gson gson = new Gson();
                ride = gson.fromJson(json, RideDetailsDTO.class);
            }
        }

        currentUserId = AuthService.getInstance().getLoggedInUserId(requireContext());
        currentUserRole = AuthService.getInstance().getLoggedInUserRole(requireContext());

        // Restore map state
        if (savedInstanceState != null) {
            savedLat = savedInstanceState.getDouble("lat", Double.NaN);
            savedLon = savedInstanceState.getDouble("lon", Double.NaN);
            savedZoom = savedInstanceState.getDouble("zoom", Double.NaN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_details, container, false);

        // Init UI
        mapFragment = view.findViewById(R.id.mapView);
        btnCancelRide = view.findViewById(R.id.btnCancelRide);

        // Setup cancel button
        btnCancelRide.setOnClickListener(v -> handleCancelRide());

        // Show/hide cancel button based on role and ride status
        setupCancelButtonVisibility();

        setMapViewAppearance(mapFragment);
        populateRideDetails(view);
        return view;
    }

    private void setupCancelButtonVisibility() {
        if (ride == null || currentUserRole == null) {
            btnCancelRide.setVisibility(View.GONE);
            return;
        }

        if ("ADMIN".equals(currentUserRole)) {
            btnCancelRide.setVisibility(View.GONE);
            return;
        }

        if ("PASSENGER".equals(currentUserRole) || "DRIVER".equals(currentUserRole)) {
            boolean canCancel = canCancelRide();
            btnCancelRide.setVisibility(canCancel ? VISIBLE : View.GONE);

            if (canCancel) {
                btnCancelRide.setText("Cancel Ride");
            }
        } else {
            btnCancelRide.setVisibility(View.GONE);
        }
    }

    private boolean canCancelRide() {
        if (ride == null || currentUserRole == null) return false;

        String status = ride.status;
        boolean isAccepted = "ACCEPTED".equals(status);
        boolean isRequested = "REQUESTED".equals(status);
        boolean isFinished = "FINISHED".equals(status);
        boolean isCancelled = "CANCELLED".equals(status);
        boolean isInProgress = "IN_PROGRESS".equals(status);

        if (isFinished || isCancelled || isInProgress) {
            return false;
        }

        return isRequested || isAccepted;
    }

    private void handleCancelRide() {
        RideActionHelper.showCancelDialog(
                requireContext(),
                ride,
                currentUserRole,
                currentUserId,
                new RideActionHelper.CancelRideCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireContext(), "Ride cancelled successfully", Toast.LENGTH_SHORT).show();
                        refreshRideDetails();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void refreshRideDetails() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void populateRideDetails(View view) {
        if (ride == null) return;

        StringBuilder strBuilder;

        TextView tvInconsistencies = view.findViewById(R.id.tvRideDetailsInconsistencies);
        strBuilder = new StringBuilder(ride.inconsistencies == null ? "-" : ride.inconsistencies);
        tvInconsistencies.setText(strBuilder.toString());

        TextView tvRatings = view.findViewById(R.id.tvRideDetailsRatings);
        if (ride.reviews == null || ride.reviews.isEmpty()) strBuilder = new StringBuilder("-");
        else {
            strBuilder = new StringBuilder();
            for (ReviewDTO review : ride.reviews){
                strBuilder.append(String.format("%s: %s\n", review.passengerName, review.comment));
                strBuilder.append(String.format("Driver rating: %d\n", review.driverRating));
                strBuilder.append(String.format("Vehicle rating: %d", review.vehicleRating));
                if (ride.reviews.indexOf(review) < ride.reviews.size() - 1) {
                    strBuilder.append("\n\n");
                }
            }
        }
        tvRatings.setText(strBuilder.toString());

        TextView tvInfo = view.findViewById(R.id.tvRideDetailsInfo);
        strBuilder = new StringBuilder();
        strBuilder.append(String.format("Route: %s → %s\n", ride.startLocation.address, ride.endLocation.address));
        strBuilder.append(String.format("Start Time: %s\n", getTimeOnly(ride.startTime)));
        strBuilder.append(String.format("End Time: %s\n", getTimeOnly(ride.endTime)));
        strBuilder.append(String.format("Status: %s\n", ride.status));
        try {
            double priceValue = Double.parseDouble(String.valueOf(ride.price));
            strBuilder.append(String.format("Price: %.2f RSD", priceValue));
        } catch (NumberFormatException e) {
            strBuilder.append(String.format("Price: %s RSD", ride.price));
        }
        tvInfo.setText(strBuilder.toString());

        TextView tvDriver = view.findViewById(R.id.tvRideDetailsDriver);
        strBuilder = new StringBuilder();
        strBuilder.append(String.format("Name: %s\n", ride.driverName));
        strBuilder.append(String.format("Car: %s", ride.vehicle));
        tvDriver.setText(strBuilder.toString());

        TextView tvPassengers = view.findViewById(R.id.tvRideDetailsPassengers);
        strBuilder = new StringBuilder();
        strBuilder.append(ride.ordererName).append("\n");
        if (ride.linkedPassengers != null) {
            for (String passenger : ride.linkedPassengers) {
                strBuilder.append(passenger).append("\n");
            }
        }
        if (strBuilder.length() > 0) {
            strBuilder.deleteCharAt(strBuilder.length() - 1);
        }
        tvPassengers.setText(strBuilder.toString());

        TextView tvCancelled = view.findViewById(R.id.tvRideDetailsCancelled);
        strBuilder = new StringBuilder();
        strBuilder.append(ride.cancelledBy == null ? "-" : String.format("By %s", ride.cancelledBy));
        tvCancelled.setText(strBuilder.toString());

        TextView tvPanic = view.findViewById(R.id.tvRideDetailsPanic);
        strBuilder = new StringBuilder();
        if (!ride.panic) strBuilder.append("-");
        else {
            strBuilder.append("Panic triggered by ").append(ride.panicTriggeredBy).append("\n");
            strBuilder.append("Panic triggered at: ").append(ride.panicTriggeredAt);
        }
        tvPanic.setText(strBuilder.toString());
    }

    private void setMapViewAppearance(MapView mapFragment) {
        if (ride == null || ride.startLocation == null || ride.endLocation == null) return;

        mapFragment.setMultiTouchControls(true);
        mapFragment.setMinZoomLevel(10.0);
        mapFragment.setMaxZoomLevel(19.0);

        if (!Double.isNaN(savedLat) && !Double.isNaN(savedLon) && !Double.isNaN(savedZoom)) {
            mapFragment.getController().setZoom(savedZoom);
            mapFragment.getController().setCenter(new GeoPoint(savedLat, savedLon));
        } else {
            GeoPoint centerPoint = new GeoPoint(
                    (ride.startLocation.latitude + ride.endLocation.latitude) / 2,
                    (ride.startLocation.longitude + ride.endLocation.longitude) / 2
            );
            mapFragment.getController().setZoom(14.5);
            mapFragment.getController().setCenter(centerPoint);
        }
        renderRoute();
        renderMarkers();
    }

    private void renderMarkers(){
        if (this.ride == null || mapFragment == null) return;

        mapFragment.getOverlays().clear();

        // start marker
        Marker startMarker = new Marker(mapFragment);
        startMarker.setPosition(new GeoPoint(
                ride.startLocation.latitude,
                ride.startLocation.longitude
        ));
        startMarker.setTitle("Start");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(
                requireContext().getDrawable(R.drawable.location_red)
        );
        mapFragment.getOverlays().add(startMarker);

        // end marker
        Marker endMarker = new Marker(mapFragment);
        endMarker.setPosition(new GeoPoint(
                ride.endLocation.latitude,
                ride.endLocation.longitude
        ));
        endMarker.setTitle("End");
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        endMarker.setIcon(
                requireContext().getDrawable(R.drawable.location_green)
        );
        mapFragment.getOverlays().add(endMarker);

        // stops markers
        if (ride.routeStops != null) {
            for (LocationDTO stop : ride.routeStops){
                Marker marker = new Marker(mapFragment);
                marker.setPosition(new GeoPoint(
                        stop.latitude,
                        stop.longitude
                ));
                marker.setTitle("Stop");
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setIcon(
                        requireContext().getDrawable(R.drawable.location_blue)
                );
                mapFragment.getOverlays().add(marker);
            }
        }
    }

    private void renderRoute(){
        if (ride == null || ride.startLocation == null || ride.endLocation == null) return;

        List<GeoPoint> points = new ArrayList<>();
        points.add(new GeoPoint(ride.startLocation.latitude, ride.startLocation.longitude));
        if (ride.routeStops != null) {
            for (LocationDTO stop : ride.routeStops){
                points.add(new GeoPoint(stop.latitude, stop.longitude));
            }
        }
        points.add(new GeoPoint(ride.endLocation.latitude, ride.endLocation.longitude));

        RouteHelper.fetchRoutePolyline(points, new RouteHelper.RouteCallback() {
            @Override
            public void onRouteReady(Polyline polyline) {
                if (mapFragment != null && isAdded()) {
                    mapFragment.getOverlays().removeIf(overlay -> overlay instanceof Polyline);
                    mapFragment.getOverlays().add(0, polyline);
                    mapFragment.invalidate();
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapFragment != null) mapFragment.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapFragment != null) mapFragment.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapFragment != null) {
            outState.putDouble("lat", mapFragment.getMapCenter().getLatitude());
            outState.putDouble("lon", mapFragment.getMapCenter().getLongitude());
            outState.putDouble("zoom", mapFragment.getZoomLevelDouble());
        }
        outState.putString("user_role", currentUserRole);
    }
}