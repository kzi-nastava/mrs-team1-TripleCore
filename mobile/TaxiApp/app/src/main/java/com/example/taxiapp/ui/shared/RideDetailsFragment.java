package com.example.taxiapp.ui.shared;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import com.example.taxiapp.R;

import java.util.List;

import model.ReviewDTO;
import model.RideDetailsDTO;
import model.LocationDTO;

public class RideDetailsFragment extends Fragment {

    private RideDetailsDTO ride = createMockRide();

    private MapView mapFragment;
    private GeoPoint startPoint = new GeoPoint(45.2671, 19.8335);
    private GeoPoint endPoint = new GeoPoint(45.2542, 19.8601);

    private double savedLat = Double.NaN;
    private double savedLon = Double.NaN;
    private double savedZoom = Double.NaN;

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

    private RideDetailsDTO createMockRide(){
        RideDetailsDTO ride1 = new RideDetailsDTO();
        ride1.id = 1L;

        // passengers
        ride1.ordererName = "Danica Komatović";
        ride1.linkedPassengers = List.of("Petar Petrović", "Milica Ilić");

        // driver
        ride1.driverName = "Marko Marković";
        ride1.vehicle = "Toyota Corolla (NS-123-AB)";

        // route
        ride1.startLocation = new LocationDTO();
        ride1.startLocation.latitude = 45.2671;
        ride1.startLocation.longitude = 19.8335;
        ride1.startLocation.address = "Bulevar Oslobođenja 1, Novi Sad";

        ride1.endLocation = new LocationDTO();
        ride1.endLocation.latitude = 45.2550;
        ride1.endLocation.longitude = 19.8450;
        ride1.endLocation.address = "Narodnog fronta 12, Novi Sad";

        LocationDTO stop1 = new LocationDTO();
        stop1.latitude = 45.2600;
        stop1.longitude = 19.8400;
        stop1.address = "Futoška 10, Novi Sad";

        ride1.routeStops = List.of(stop1);

        // time
        ride1.startTime = "2026-01-12T14:30";
        ride1.endTime = "2026-01-12T14:55";

        // panic
        ride1.panic = false;
        ride1.panicTriggeredBy = null;
        ride1.panicTriggeredAt = null;

        // other info
        ride1.price = 650.0;
        ride1.status = "FINISHED";
        ride1.cancelledBy = null;
        ride1.inconsistencies = null;

        ReviewDTO review1 = new ReviewDTO();
        review1.rideId = 1L;
        review1.passengerId = 10L;
        review1.passengerName = "Petar Petrović";
        review1.driverId = 100L;
        review1.driverName = "Marko Marković";
        review1.driverRating = 5;
        review1.vehicleRating = 4;
        review1.comment = "Vožnja je bila prijatna i brza.";

        ride1.reviews = List.of(review1);

        return ride1;
    }
}
