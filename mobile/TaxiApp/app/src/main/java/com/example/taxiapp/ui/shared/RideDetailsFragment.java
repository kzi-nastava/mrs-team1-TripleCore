package com.example.taxiapp.ui.shared;

import static helper.DateTimeHelper.getTimeOnly;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import com.example.taxiapp.R;

import java.util.ArrayList;
import java.util.List;

import helper.RouteHelper;
import model.ReviewDTO;
import model.RideDetailsDTO;
import model.LocationDTO;

public class RideDetailsFragment extends Fragment {

    private RideDetailsDTO ride;
    private MapView mapFragment;
    private double savedLat = Double.NaN;
    private double savedLon = Double.NaN;
    private double savedZoom = Double.NaN;

    public RideDetailsFragment(RideDetailsDTO rideDetails){
        this.ride = rideDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ride_details, container, false);

        mapFragment = view.findViewById(R.id.mapView);

        if (savedInstanceState != null) {
            savedLat = savedInstanceState.getDouble("lat", Double.NaN);
            savedLon = savedInstanceState.getDouble("lon", Double.NaN);
            savedZoom = savedInstanceState.getDouble("zoom", Double.NaN);
        }

        setMapViewAppearance(mapFragment);
        populateRideDetails(view);
        return view;
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

    // when the device is rotated save the state of the map
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapFragment != null) {
            outState.putDouble("lat", mapFragment.getMapCenter().getLatitude());
            outState.putDouble("lon", mapFragment.getMapCenter().getLongitude());
            outState.putDouble("zoom", mapFragment.getZoomLevelDouble());
        }
    }

    private void populateRideDetails(View view){
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
            }
        }
        tvRatings.setText(strBuilder.toString());

        TextView tvInfo = view.findViewById(R.id.tvRideDetailsInfo);
        strBuilder = new StringBuilder();
        strBuilder.append(String.format("Route: %s → %s\n", ride.startLocation.address, ride.endLocation.address));
        strBuilder.append(String.format("Start Time: %s\n", getTimeOnly(ride.startTime)));
        strBuilder.append(String.format("End Time: %s\n", getTimeOnly(ride.endTime)));
        strBuilder.append(String.format("Status: %s\n", ride.status));
        strBuilder.append(String.format("Price: %s", ride.price));
        tvInfo.setText(strBuilder.toString());

        TextView tvDriver = view.findViewById(R.id.tvRideDetailsDriver);
        strBuilder = new StringBuilder();
        strBuilder.append(String.format("Name: %s\n", ride.driverName));
        strBuilder.append(String.format("Car: %s", ride.vehicle));
        tvDriver.setText(strBuilder.toString());

        TextView tvPassengers = view.findViewById(R.id.tvRideDetailsPassengers);
        strBuilder = new StringBuilder();
        strBuilder.append(ride.ordererName).append("\n");
        for (String passenger : ride.linkedPassengers) strBuilder.append(passenger).append("\n");
        strBuilder.deleteCharAt(strBuilder.length() - 1);
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
            strBuilder.append("Panic triggered at: ").append(ride.panicTriggeredAt).append("\n");
        }
        tvPanic.setText(strBuilder.toString());
    }


    private void setMapViewAppearance(MapView mapFragment) {

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
}
