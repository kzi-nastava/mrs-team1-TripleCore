package com.example.taxiapp.ui.ride_tracking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.taxiapp.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import helper.RouteHelper;
import model.LocationDTO;
import model.RideDetailsDTO;
import model.RideTrackingInfo;


public class RideTrackingFragment extends Fragment {

    private MapView mapFragment;
    private double savedLat = Double.NaN;
    private double savedLon = Double.NaN;
    private double savedZoom = Double.NaN;

    private TextView tvRideInfo, tvTrackingInfo;
    private Button btnFinishRide;
    private Button btnPanic;

    private RideDetailsDTO ride;
    private RideTrackingInfo trackingInfo = createMockTrackingInfo();
    private String role = "DRIVER";// "DRIVER" ili "PASSENGER"

    public RideTrackingFragment(RideDetailsDTO rideDetails){
        ride = rideDetails;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ride_tracking, container, false);

        mapFragment = view.findViewById(R.id.ride_tracking_map_view);
        tvRideInfo = view.findViewById(R.id.tv_ride_info);
        tvTrackingInfo = view.findViewById(R.id.tv_tracking_info);
        btnFinishRide = view.findViewById(R.id.btn_ride_tracking_finish_ride);
        btnPanic = view.findViewById(R.id.btn_ride_tracking_panic);

        setupMap();
        bindRideData();
        bindTrackingInfo();
        setupActions();

        return view;
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

    private void setupMap() {
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
        if (this.ride == null) return;

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
        for (LocationDTO stop : ride.routeStops){
            Marker marker = new Marker(mapFragment);
            marker.setPosition(new GeoPoint(
                    stop.latitude,
                    stop.longitude
            ));
            marker.setTitle("End");
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(
                    requireContext().getDrawable(R.drawable.location_blue)
            );
            mapFragment.getOverlays().add(marker);
        }
    }

    private void renderRoute(){
        List<GeoPoint> points = new ArrayList<>();
        points.add(new GeoPoint(ride.startLocation.latitude, ride.startLocation.longitude));
        for (LocationDTO stop : ride.routeStops){
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

    private void bindRideData() {
        if (ride == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("Route: ")
                .append(ride.startLocation.address)
                .append(" → ")
                .append(ride.endLocation.address)
                .append("\n");

        sb.append("Driver: ").append(ride.driverName).append("\n");
        sb.append("Vehicle: ").append(ride.vehicle).append("\n");
        sb.append("Passengers: ").append(ride.ordererName).append("\n");

        if (ride.linkedPassengers != null) {
            for (String p : ride.linkedPassengers) {
                sb.append(p).append("\n");
            }
        }

        tvRideInfo.setText(sb.toString());
    }

    private void bindTrackingInfo() {
        if (trackingInfo == null) {
            tvTrackingInfo.setVisibility(View.GONE);
            return;
        }

        double km = trackingInfo.estimatedDistance / 1000.0;
        double min = trackingInfo.estimatedTime / 60.0;

        String text =
                "Estimated Distance: " + String.format(Locale.US, "%.2f", km) + " km\n" +
                        "Estimated Time: " + String.format(Locale.US, "%.2f", min) + " mins";

        tvTrackingInfo.setText(text);
    }

    private void setupActions() {
        if ("DRIVER".equals(role)) {
            btnFinishRide.setVisibility(View.VISIBLE);
            btnFinishRide.setOnClickListener(v -> finishRide());
        }
        if ("PASSENGER".equals(role)) {
            btnFinishRide.setVisibility(View.VISIBLE);
            btnFinishRide.setOnClickListener(v -> panic());
        }
    }

    private void finishRide() {
        // poziv backend API-ja
    }

    private void panic(){

    }


    private RideTrackingInfo createMockTrackingInfo() {
        RideTrackingInfo info = new RideTrackingInfo();

        info.rideId = 1L;
        info.vehicleId = 99L;

        info.vehicleLocation = new LocationDTO();
        info.vehicleLocation.latitude = 44.7990;
        info.vehicleLocation.longitude = 20.4600;
        info.vehicleLocation.address = "Bulevar kralja Aleksandra";

        info.estimatedDistance = 8200; // metri
        info.estimatedTime = 780L;     // sekunde (~13 min)

        return info;
    }


}
