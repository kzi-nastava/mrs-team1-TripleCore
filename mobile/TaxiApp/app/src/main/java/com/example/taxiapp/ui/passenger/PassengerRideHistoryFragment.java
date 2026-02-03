package com.example.taxiapp.ui.passenger;

import android.app.DatePickerDialog;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import helper.RideFilterHelper;
import helper.ShakeDetector;
import model.RideDetailsDTO;
import service.PassengerService;
import service.AuthService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRideHistoryFragment extends Fragment {

    private LinearLayout cardsContainer;
    private TextInputEditText etDateFrom, etDateTo, etTextFilter;
    private Button btnClear, btnApply;

    private List<RideDetailsDTO> allRides = new ArrayList<>();

    // SENSOR SHAKE
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;

    private boolean sortDescending = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_passenger_ride_history, container, false);

        // UI
        etDateFrom = view.findViewById(R.id.etDateFrom);
        etDateTo = view.findViewById(R.id.etDateTo);
        etTextFilter = view.findViewById(R.id.etTextFilter);
        btnClear = view.findViewById(R.id.btnClear);
        btnApply = view.findViewById(R.id.btnApply);
        cardsContainer = view.findViewById(R.id.cards_container);

        etDateFrom.setOnClickListener(v -> showDatePicker(etDateFrom));
        etDateTo.setOnClickListener(v -> showDatePicker(etDateTo));

        btnClear.setOnClickListener(v -> clearInputs());
        btnApply.setOnClickListener(v -> applyFiltersAndSort());

        // SENSOR SHAKE init
        sensorManager = (SensorManager) requireContext().getSystemService(requireContext().SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(() -> applyFiltersAndSort());

        fetchRidesFromBackend();

        return view;
    }

    private void fetchRidesFromBackend() {
        Long passengerId = AuthService.getInstance().getLoggedInUserId(requireContext());

        if (passengerId == -1) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        PassengerService.getInstance().getRideHistory(passengerId, new Callback<List<RideDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<RideDetailsDTO>> call, Response<List<RideDetailsDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allRides = response.body();
                    populateRideCards(allRides);
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch rides", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RideDetailsDTO>> call, Throwable t) {
                Toast.makeText(requireContext(), "Failed to fetch rides: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateRideCards(List<RideDetailsDTO> rides) {
        cardsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (RideDetailsDTO ride : rides) {
            MaterialCardView card = (MaterialCardView) inflater.inflate(R.layout.view_passenger_ride_card, cardsContainer, false);

            TextView tvRoute = card.findViewById(R.id.tvPassengerRoute);
            TextView tvStart = card.findViewById(R.id.tvPassengerStartAddress);
            TextView tvEnd = card.findViewById(R.id.tvPassengerEndAddress);
            TextView tvDateTime = card.findViewById(R.id.tvPassengerDateTime);
            TextView tvPrice = card.findViewById(R.id.tvPassengerPrice);
            TextView tvStatus = card.findViewById(R.id.tvPassengerStatus);
            TextView tvPanic = card.findViewById(R.id.tvPassengerPanic);

            // Route
            if (ride.startLocation != null && ride.endLocation != null) {
                tvRoute.setText(ride.startLocation.address + " → " + ride.endLocation.address);
                tvStart.setText(ride.startLocation.address);
                tvEnd.setText(ride.endLocation.address);
            }

            // Date and Time
            String start = ride.startTime != null ? ride.startTime : "N/A";
            String end = ride.endTime != null ? ride.endTime : "N/A";
            tvDateTime.setText(start + " - " + end);

            // Price
            tvPrice.setText(ride.price + " RSD");

            // Status
            if (ride.status != null) {
                switch (ride.status) {
                    case "FINISHED":
                    case "IN_PROGRESS":
                    case "ACCEPTED":
                        tvStatus.setText(ride.status);
                        tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
                        break;
                    case "CANCELLED":
                        tvStatus.setText(ride.status);
                        tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
                        break;
                    default:
                        tvStatus.setText(ride.status);
                        tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
                }
            }

            // Panic badge
            tvPanic.setVisibility(ride.panic ? View.VISIBLE : View.GONE);

            // Card click opens ride details fragment
            card.setOnClickListener(v -> openRideDetails(ride.id));

            cardsContainer.addView(card);
        }
    }

    private void showDatePicker(TextInputEditText dateInput) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    dateInput.setText(sdf.format(selectedDate.getTime()));
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void clearInputs() {
        etTextFilter.setText("");
        etDateFrom.setText("");
        etDateTo.setText("");
        populateRideCards(allRides);
    }

    private void applyFiltersAndSort() {
        String searchText = etTextFilter.getText().toString();
        String dateFrom = etDateFrom.getText().toString();
        String dateTo = etDateTo.getText().toString();

        List<RideDetailsDTO> result =
                RideFilterHelper.filterAndSortRides(
                        allRides,
                        searchText,
                        dateFrom,
                        dateTo,
                        sortDescending
                );

        populateRideCards(result);

        Toast.makeText(
                requireContext(),
                sortDescending ? "Sorted by newest first" : "Sorted by oldest first",
                Toast.LENGTH_SHORT
        ).show();

        sortDescending = !sortDescending;
    }

    private void openRideDetails(Long rideId) {
        PassengerRideDetailsFragment fragment = PassengerRideDetailsFragment.newInstance(Math.toIntExact(rideId));
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(
                    shakeDetector,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_UI
            );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }
}
