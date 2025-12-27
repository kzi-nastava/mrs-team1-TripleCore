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

public class RideDetailsFragment extends Fragment {

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
                    (startPoint.getLatitude() + endPoint.getLatitude()) / 2,
                    (startPoint.getLongitude() + endPoint.getLongitude()) / 2
            );
            mapFragment.getController().setZoom(14.5);
            mapFragment.getController().setCenter(centerPoint);
        }

        // Dodavanje markera
        Marker startMarker = new Marker(mapFragment);
        startMarker.setPosition(startPoint);
        startMarker.setTitle("Start");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapFragment.getOverlays().add(startMarker);

        Marker endMarker = new Marker(mapFragment);
        endMarker.setPosition(endPoint);
        endMarker.setTitle("End");
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapFragment.getOverlays().add(endMarker);
    }
}
