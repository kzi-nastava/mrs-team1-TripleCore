package com.example.taxiapp.ui.driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.shared.RideHistoryFragment;

import java.util.ArrayList;
import java.util.List;

import model.LocationDTO;
import model.ReviewDTO;
import model.RideDetailsDTO;

public class DriverRideHistoryFragment extends Fragment {

    private List<RideDetailsDTO> rideHistory = createMockRides();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_ride_history, container, false);

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new RideHistoryFragment(rideHistory))
                    .commit();
        }

        return view;
    }

    private List<RideDetailsDTO> createMockRides() {
        List<RideDetailsDTO> rides = new ArrayList<>();

        // ===== RIDE 1 =====
        RideDetailsDTO ride1 = new RideDetailsDTO();
        ride1.id = 1L;

        ride1.ordererName = "Danica Komatović";
        ride1.linkedPassengers = List.of("Petar Petrović", "Milica Ilić");

        ride1.driverName = "Marko Marković";
        ride1.vehicle = "Toyota Corolla (NS-123-AB)";

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

        ride1.startTime = "2026-01-12T14:30";
        ride1.endTime = "2026-01-12T14:55";

        ride1.price = 650.0;
        ride1.status = "FINISHED";

        ReviewDTO r1 = new ReviewDTO();
        r1.passengerName = "Petar Petrović";
        r1.driverRating = 5;
        r1.vehicleRating = 4;
        r1.comment = "Vožnja je bila prijatna i brza.";

        ride1.reviews = List.of(r1);

        rides.add(ride1);

        // ===== RIDE 2 =====
        RideDetailsDTO ride2 = new RideDetailsDTO();
        ride2.id = 2L;

        ride2.ordererName = "Ana Jovanović";
        ride2.linkedPassengers = List.of();

        ride2.driverName = "Nikola Ilić";
        ride2.vehicle = "Škoda Octavia (BG-456-CD)";

        ride2.startLocation = new LocationDTO();
        ride2.startLocation.latitude = 45.2512;
        ride2.startLocation.longitude = 19.8369;
        ride2.startLocation.address = "Zmaj Jovina 5, Novi Sad";

        ride2.endLocation = new LocationDTO();
        ride2.endLocation.latitude = 45.2401;
        ride2.endLocation.longitude = 19.8223;
        ride2.endLocation.address = "Bulevar Evrope 44, Novi Sad";

        ride2.routeStops = List.of();

        ride2.startTime = "2026-01-13T09:10";
        ride2.endTime = "2026-01-13T09:32";

        ride2.price = 420.0;
        ride2.status = "FINISHED";

        ride2.reviews = List.of(); // nema recenzija

        rides.add(ride2);

        // ===== RIDE 3 =====
        RideDetailsDTO ride3 = new RideDetailsDTO();
        ride3.id = 3L;

        ride3.ordererName = "Milan Stojanović";
        ride3.linkedPassengers = List.of("Ivana Stojanović");

        ride3.driverName = "Jovan Petrović";
        ride3.vehicle = "VW Passat (NS-789-EF)";

        ride3.startLocation = new LocationDTO();
        ride3.startLocation.latitude = 45.2608;
        ride3.startLocation.longitude = 19.8512;
        ride3.startLocation.address = "Liman 3, Novi Sad";

        ride3.endLocation = new LocationDTO();
        ride3.endLocation.latitude = 45.2739;
        ride3.endLocation.longitude = 19.8201;
        ride3.endLocation.address = "Detelinara, Novi Sad";

        ride3.routeStops = List.of();

        ride3.startTime = "2026-01-14T18:45";
        ride3.endTime = "2026-01-14T19:05";

        ride3.price = 510.0;
        ride3.status = "CANCELLED";
        ride3.cancelledBy = "DRIVER";

        rides.add(ride3);

        return rides;
    }

}