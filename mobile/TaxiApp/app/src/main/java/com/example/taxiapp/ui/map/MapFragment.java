package com.example.taxiapp.ui.map;
import com.example.taxiapp.R;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.lights.LightsManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import model.ActiveVehicleLocationResponse;

public class MapFragment extends Fragment {

    private MapView mapView;
    private List<ActiveVehicleLocationResponse> vehicleLocations = new ArrayList<>();

    public MapFragment(){}
    public MapFragment(List<ActiveVehicleLocationResponse> vehicleLocations){
        this.vehicleLocations = vehicleLocations;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        Configuration.getInstance()
                .setUserAgentValue(requireContext().getPackageName());

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapView);

        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        GeoPoint startPoint = new GeoPoint( 45.25167, 19.83694);
        MapController mapController = (MapController) mapView.getController();
        mapController.setZoom(16.0);
        mapController.setCenter(startPoint);

        // Scaling the icon for vehicle display
        Drawable taxiIcon = ContextCompat.getDrawable(requireContext(), R.drawable.taxi_no_shadow);
        Bitmap bitmap = ((BitmapDrawable) taxiIcon).getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 32, 32, false);

        for (ActiveVehicleLocationResponse vehicle : vehicleLocations) {
            GeoPoint point = new GeoPoint(vehicle.latitude, vehicle.longitude);

            Marker marker = new Marker(mapView);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(new BitmapDrawable(getResources(), scaledBitmap));
            marker.setSubDescription(
                    vehicle.available ? "Available" : "Unavailable"
            );

            mapView.getOverlays().add(marker);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
}
