package com.example.taxiapp.ui.ride_history;

import static android.view.View.VISIBLE;
import static helper.DateTimeHelper.getTimeOnly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.google.gson.Gson;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import helper.CancelRideHelper;
import helper.RouteHelper;
import model.RideDetailsDTO;
import model.LocationDTO;
import service.AuthService;

public class RideDetailsFragment extends Fragment {

    private static final String ARG_RIDE_DETAILS = "ride_details";

    private RideDetailsDTO ride;
    private String currentUserRole;
    private Long currentUserId;

    private MapView mapFragment;
    private Button btnCancelRide;

    private Double savedLat = null;
    private Double savedLon = null;
    private Double savedZoom = null;

    public RideDetailsFragment() {}

    public static RideDetailsFragment newInstance(RideDetailsDTO rideDetails, String userRole) {
        RideDetailsFragment fragment = new RideDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_RIDE_DETAILS, rideDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ride = (RideDetailsDTO) getArguments().getSerializable(ARG_RIDE_DETAILS);
        }

        currentUserId = AuthService.getInstance().getLoggedInUserId(requireContext());
        currentUserRole = AuthService.getInstance().getLoggedInUserRole(requireContext());

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("lat")) {
                savedLat = savedInstanceState.getDouble("lat");
            }
            if (savedInstanceState.containsKey("lon")) {
                savedLon = savedInstanceState.getDouble("lon");
            }
            if (savedInstanceState.containsKey("zoom")) {
                savedZoom = savedInstanceState.getDouble("zoom");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ride_details, container, false);

        mapFragment = view.findViewById(R.id.mapView);
        btnCancelRide = view.findViewById(R.id.btnCancelRide);

        btnCancelRide.setOnClickListener(v -> handleCancelRide());

        setupCancelButtonVisibility();
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

    private void populateRideDetails(View view) {
        if (ride == null) return;

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
            org.osmdroid.api.IGeoPoint center = mapFragment.getMapCenter();
            double zoom = mapFragment.getZoomLevelDouble();

            if (center != null) {
                outState.putDouble("lat", center.getLatitude());
                outState.putDouble("lon", center.getLongitude());
            }
            outState.putDouble("zoom", zoom);
        }
    }

    private void refresh() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}