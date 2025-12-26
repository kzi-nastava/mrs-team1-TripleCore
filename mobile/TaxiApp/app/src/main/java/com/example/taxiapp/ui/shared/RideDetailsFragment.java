package com.example.taxiapp.ui.shared;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import com.example.taxiapp.R;

import java.util.ArrayList;
import java.util.List;

public class RideDetailsFragment extends Fragment {

    private MapView mapFragment;
    private GeoPoint startPoint = new GeoPoint(45.2671, 19.8335);;
    private GeoPoint endPoint = new GeoPoint(45.2542, 19.8601);;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_details, container, false);

        // Dodaj mapu u container
        if (savedInstanceState == null) {
            mapFragment = new MapView(requireContext());
            mapFragment.setId(View.generateViewId());
            ((ViewGroup) view.findViewById(R.id.map_fragment_container)).addView(mapFragment);
            setMapViewAppearance(mapFragment);

        }

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

    private void setMapViewAppearance(MapView mapFragment){

        GeoPoint centerPoint = new GeoPoint((startPoint.getLatitude() + endPoint.getLatitude()) / 2,
                (startPoint.getLongitude() + endPoint.getLongitude()) / 2);

        mapFragment.setMultiTouchControls(true);
        mapFragment.getController().setZoom(14.5);
        mapFragment.getController().setCenter(centerPoint);

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
