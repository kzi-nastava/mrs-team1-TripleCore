package com.example.taxiapp.ui.start_ride;
import android.content.Context;
import android.content.SharedPreferences;
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

import model.LocationDTO;
import model.RideResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.RideService;
import com.example.taxiapp.R;

public class StartRideFragment extends Fragment {

    private TextView tvRideHeader, tvStartPoint, tvEndPoint, tvStations, tvNoRide;
    private View rideCard;
    private Button btnStartRide;

    private RideResponse currentRide = null;
    private long driverId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_ride, container, false);

        rideCard = view.findViewById(R.id.rideCard);
        tvNoRide = view.findViewById(R.id.tvNoRide);
        tvRideHeader = view.findViewById(R.id.tvRideHeader);
        tvStartPoint = view.findViewById(R.id.tvStartPoint);
        tvEndPoint = view.findViewById(R.id.tvEndPoint);
        tvStations = view.findViewById(R.id.tvStations);
        btnStartRide = view.findViewById(R.id.btnStartRide);

        SharedPreferences prefs = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        driverId = prefs.getLong("userId", -1);

        if (driverId == -1) {
            Log.e("StartRideFragment", "Driver ID fali u SharedPreferences!");
        }

        loadRideData();

        btnStartRide.setOnClickListener(v -> startRideClick());

        return view;
    }

    private void loadRideData() {
        Log.d("DEBUG_RIDE", "Pozivam API za driverId: " + driverId);

        RideService.getInstance().getRideToStart(driverId, new Callback<RideResponse>() {
            @Override
            public void onResponse(Call<RideResponse> call, Response<RideResponse> response) {
                Log.d("DEBUG_RIDE", "Status kod: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    currentRide = response.body();
                    Log.d("DEBUG_RIDE", "Vožnja pronađena: " + currentRide.getId());
                    displayRide(currentRide);
                } else {
                    Log.d("DEBUG_RIDE", "Backend vratio prazno ili grešku. Body: " + response.errorBody());
                    showNoRide();
                }
            }

            @Override
            public void onFailure(Call<RideResponse> call, Throwable t) {
                Log.e("DEBUG_RIDE", "TOTALNI FAIL (Mreža ili GSON): " + t.getMessage());
                t.printStackTrace();
                showNoRide();
            }
        });
    }

    private void displayRide(RideResponse ride) {
        rideCard.setVisibility(View.VISIBLE);
        tvNoRide.setVisibility(View.GONE);

        if (ride.getStartLocation() != null && ride.getEndLocation() != null) {
            String header = ride.getStartLocation().address + " - " + ride.getEndLocation().address;
            tvRideHeader.setText(header);
            tvStartPoint.setText("Start Point: " + ride.getStartLocation().address);
            tvEndPoint.setText("Destination Point: " + ride.getEndLocation().address);
        }

        if (ride.getRouteStops() != null && !ride.getRouteStops().isEmpty()) {
            StringBuilder sb = new StringBuilder("Stations:\n");
            for (LocationDTO stopLoc : ride.getRouteStops()) {
                sb.append("• ").append(stopLoc.address).append("\n");
            }
            tvStations.setText(sb.toString());
            tvStations.setVisibility(View.VISIBLE);
        } else {
            tvStations.setVisibility(View.GONE);
        }
    }

    private void showNoRide() {
        rideCard.setVisibility(View.GONE);
        tvNoRide.setVisibility(View.VISIBLE);
    }

    private void startRideClick() {
        if (currentRide == null) return;


        RideService.getInstance().startRide(currentRide.getId(), driverId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Ride started!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Failed to start ride: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}