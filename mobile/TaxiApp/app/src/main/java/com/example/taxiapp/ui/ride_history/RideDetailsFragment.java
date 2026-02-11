package com.example.taxiapp.ui.ride_history;

import static android.view.View.VISIBLE;
import static helper.DateTimeHelper.getTimeOnly;

import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.review.ReviewFormFragment;
import com.google.gson.Gson;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import helper.CancelRideHelper;
import helper.RouteHelper;
import helper.StopRideHelper;
import model.ReviewDTO;
import model.RideDetailsDTO;
import model.LocationDTO;
import model.StopRideResponse;
import service.AuthService;

public class RideDetailsFragment extends Fragment {

    private static final String ARG_RIDE_DETAILS = "ride_details";
    private static final int REQUEST_CODE_LOCATION = 100;

    private RideDetailsDTO ride;
    private String currentUserRole; // DRIVER, PASSENGER, ADMIN
    private Long currentUserId;

    private MapView mapFragment;
    private Button btnCancelRide;
    private Button btnStopRide;
    private Button btnReview;

    private double savedLat = Double.NaN;
    private double savedLon = Double.NaN;
    private double savedZoom = Double.NaN;

    public RideDetailsFragment() {}

    public static RideDetailsFragment newInstance(RideDetailsDTO rideDetails, String userRole) {
        RideDetailsFragment fragment = new RideDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RIDE_DETAILS, new Gson().toJson(rideDetails));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String json = getArguments().getString(ARG_RIDE_DETAILS);
            if (json != null) ride = new Gson().fromJson(json, RideDetailsDTO.class);
        }

        currentUserId = AuthService.getInstance().getLoggedInUserId(requireContext());
        currentUserRole = AuthService.getInstance().getLoggedInUserRole(requireContext());

        if (savedInstanceState != null) {
            savedLat = savedInstanceState.getDouble("lat", Double.NaN);
            savedLon = savedInstanceState.getDouble("lon", Double.NaN);
            savedZoom = savedInstanceState.getDouble("zoom", Double.NaN);
        }

        // setting a fragment result listener for the new review
        // so we don't need to fetch the whole ride from the backend for the review we created
        getParentFragmentManager().setFragmentResultListener(
                "reviewRequestKey",
                this,
                (requestKey, bundle) -> {

                    ReviewDTO newReview =
                            (ReviewDTO) bundle.getSerializable("newReview");

                    if (newReview != null) {
                        onNewReviewReceived(newReview);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ride_details, container, false);

        mapFragment = view.findViewById(R.id.mapView);
        btnCancelRide = view.findViewById(R.id.btnCancelRide);
        btnStopRide = view.findViewById(R.id.btnStopRide);
        btnReview = view.findViewById(R.id.btnReview);

        btnCancelRide.setOnClickListener(v -> handleCancelRide());
        btnStopRide.setOnClickListener(v -> handleStopRide());
        btnReview.setOnClickListener(v -> handleReview());

        setupCancelButtonVisibility();
        setupStopButtonVisibility();
        setupReviewButtonVisibility();

        setMapViewAppearance(mapFragment);
        populateRideDetails(view);

        return view;
    }

    /* ===================== CANCEL RIDE ===================== */

    private void setupCancelButtonVisibility() {
        if (ride == null || currentUserRole == null) {
            btnCancelRide.setVisibility(View.GONE);
            return;
        }

        if ("ADMIN".equals(currentUserRole)) {
            btnCancelRide.setVisibility(View.GONE);
            return;
        }

        boolean canCancel = canCancelRide();
        btnCancelRide.setVisibility(canCancel ? VISIBLE : View.GONE);
    }

    private boolean canCancelRide() {
        if (ride == null) return false;
        return "REQUESTED".equals(ride.status) || "ACCEPTED".equals(ride.status);
    }

    private void handleCancelRide() {
        CancelRideHelper.showCancelDialog(
                requireContext(),
                ride,
                currentUserRole,
                currentUserId,
                new CancelRideHelper.CancelRideCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireContext(),
                                "Ride cancelled successfully",
                                Toast.LENGTH_SHORT).show();
                        refresh();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    /* ===================== STOP RIDE ===================== */

    private void setupStopButtonVisibility() {
        if (ride == null || currentUserRole == null) {
            btnStopRide.setVisibility(View.GONE);
            return;
        }

        boolean show = "DRIVER".equals(currentUserRole) && "IN_PROGRESS".equals(ride.status);
        btnStopRide.setVisibility(show ? VISIBLE : View.GONE);
    }

    private void handleStopRide() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION
            );
        } else {
            showStopDialogWithLocation();
        }
    }

    /* ===================== REVIEWS ===================== */

    private void setupReviewButtonVisibility(){
        if (ride == null || currentUserRole == null) {
            btnStopRide.setVisibility(View.GONE);
            return;
        }

        boolean show = "PASSENGER".equals(currentUserRole) &&
                "FINISHED".equals(ride.status) &&
                ride.reviews.stream()
                        .noneMatch(r -> currentUserId.equals(r.passengerId));
        btnReview.setVisibility(show ? VISIBLE : View.GONE);
    }

    private void populateRatings(View view) {

        TextView tvRatings = view.findViewById(R.id.tvRideDetailsRatings);

        if (ride.reviews == null || ride.reviews.isEmpty()) {
            tvRatings.setText("No ratings yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (ReviewDTO r : ride.reviews) {
            sb.append("Passenger: ").append(r.passengerName).append("\n")
                    .append("Driver rating: ").append(r.driverRating).append("\n")
                    .append("Vehicle rating: ").append(r.vehicleRating).append("\n")
                    .append("Comment: ").append(r.comment).append("\n\n");
        }

        tvRatings.setText(sb.toString().trim());
    }


    private void handleReview() {

        ReviewFormFragment reviewFragment =
                ReviewFormFragment.newInstance(ride);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, reviewFragment)
                .addToBackStack(null)
                .commit();
    }


    private void onNewReviewReceived(ReviewDTO review) {

        if (ride.reviews == null) {
            ride.reviews = new ArrayList<>();
        }

        ride.reviews.add(review);
        btnReview.setVisibility(View.GONE);
//        populateRatings(view);

        Toast.makeText(requireContext(),
                "Review successfully added",
                Toast.LENGTH_SHORT).show();

    }



    private void showStopDialogWithLocation() {
        StopRideHelper.showStopDialog(
                requireContext(),
                ride,
                new StopRideHelper.StopRideCallback() {
                    @Override
                    public void onSuccess(StopRideResponse response) {
                        Toast.makeText(requireContext(),
                                "Ride stopped successfully",
                                Toast.LENGTH_SHORT).show();
                        refresh();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showStopDialogWithLocation();
            } else {
                Toast.makeText(requireContext(),
                        "Location permission is required to stop the ride",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateRideDetails(View view) {
        if (ride == null) return;

        TextView tvInconsistencies = view.findViewById(R.id.tvRideDetailsInconsistencies);
        if (ride.inconsistencies != null && !ride.inconsistencies.isEmpty()) {
            tvInconsistencies.setText(ride.inconsistencies);
        } else {
            tvInconsistencies.setText("No inconsistencies");
        }

        TextView tvCancelled = view.findViewById(R.id.tvRideDetailsCancelled);
        if (ride.cancelledBy != null && !ride.cancelledBy.isEmpty()) {
            tvCancelled.setText(ride.cancelledBy);
        } else {
            tvCancelled.setText("Ride not cancelled");
        }

        TextView tvPanic = view.findViewById(R.id.tvRideDetailsPanic);
        if (ride.panic) {
            String panicInfo = "Triggered by: " +
                    (ride.panicTriggeredBy != null ? ride.panicTriggeredBy : "Unknown") +
                    "\nAt: " +
                    (ride.panicTriggeredAt != null ? getTimeOnly(ride.panicTriggeredAt) : "Unknown time");
            tvPanic.setText(panicInfo);
        } else {
            tvPanic.setText("Panic not triggered");
        }

        TextView tvInfo = view.findViewById(R.id.tvRideDetailsInfo);
        tvInfo.setText(
                "Route: " + ride.startLocation.address + " → " + ride.endLocation.address + "\n" +
                        "Start: " + getTimeOnly(ride.startTime) + "\n" +
                        "End: " + getTimeOnly(ride.endTime) + "\n" +
                        "Status: " + ride.status + "\n" +
                        "Price: " + ride.price + " RSD"
        );

        TextView tvDriver = view.findViewById(R.id.tvRideDetailsDriver);
        tvDriver.setText("Driver: " + ride.driverName + "\nVehicle: " + ride.vehicle);

        TextView tvPassengers = view.findViewById(R.id.tvRideDetailsPassengers);
        StringBuilder sb = new StringBuilder(ride.ordererName + "\n");
        if (ride.linkedPassengers != null) {
            for (String p : ride.linkedPassengers) sb.append(p).append("\n");
        }
        tvPassengers.setText(sb.toString().trim());
        populateRatings(view);
    }

    /* ===================== MAP ===================== */

    private void setMapViewAppearance(MapView mapFragment) {
        if (ride == null) return;

        mapFragment.setMultiTouchControls(true);
        mapFragment.setMinZoomLevel(10.0);
        mapFragment.setMaxZoomLevel(19.0);

        GeoPoint center = new GeoPoint(
                (ride.startLocation.latitude + ride.endLocation.latitude) / 2,
                (ride.startLocation.longitude + ride.endLocation.longitude) / 2
        );

        mapFragment.getController().setZoom(14.5);
        mapFragment.getController().setCenter(center);

        renderMarkers();
        renderRoute();
    }

    private void renderMarkers() {
        mapFragment.getOverlays().clear();

        addMarker(ride.startLocation, "Start", R.drawable.location_red);
        addMarker(ride.endLocation, "End", R.drawable.location_green);

        if (ride.routeStops != null) {
            for (LocationDTO stop : ride.routeStops) {
                addMarker(stop, "Stop", R.drawable.location_blue);
            }
        }
    }

    private void addMarker(LocationDTO loc, String title, int icon) {
        Marker m = new Marker(mapFragment);
        m.setPosition(new GeoPoint(loc.latitude, loc.longitude));
        m.setTitle(title);
        m.setIcon(requireContext().getDrawable(icon));
        mapFragment.getOverlays().add(m);
    }

    private void renderRoute() {
        List<GeoPoint> points = new ArrayList<>();
        points.add(new GeoPoint(ride.startLocation.latitude, ride.startLocation.longitude));
        if (ride.routeStops != null) {
            for (LocationDTO stop : ride.routeStops)
                points.add(new GeoPoint(stop.latitude, stop.longitude));
        }
        points.add(new GeoPoint(ride.endLocation.latitude, ride.endLocation.longitude));

        RouteHelper.fetchRoutePolyline(points, new RouteHelper.RouteCallback() {
            @Override
            public void onRouteReady(Polyline polyline) {
                mapFragment.getOverlays().add(0, polyline);
                mapFragment.invalidate();
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
    }

    private void refresh() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}