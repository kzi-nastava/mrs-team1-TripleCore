package com.example.taxiapp.ui.passenger;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import model.RideDetailsDTO;
import service.PassengerService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRideHistoryFragment extends Fragment {

    private LinearLayout cardsContainer;
    private TextInputEditText etDateFrom, etDateTo, etTextFilter;
    private Button btnClear, btnApply;

    private List<RideDetailsDTO> allRides = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passenger_ride_history, container, false);

        etDateFrom = view.findViewById(R.id.etDateFrom);
        etDateTo = view.findViewById(R.id.etDateTo);
        etTextFilter = view.findViewById(R.id.etTextFilter);
        btnClear = view.findViewById(R.id.btnClear);
        btnApply = view.findViewById(R.id.btnApply);
        cardsContainer = view.findViewById(R.id.cards_container);

        etDateFrom.setOnClickListener(v -> showDatePicker(etDateFrom));
        etDateTo.setOnClickListener(v -> showDatePicker(etDateTo));

        btnClear.setOnClickListener(v -> clearInputs());
        btnApply.setOnClickListener(v -> applyFilters());

        fetchRidesFromBackend();

        return view;
    }

    private void fetchRidesFromBackend() {
        Long passengerId =
                PassengerService.getInstance().getLoggedInUserId(requireContext());

        if (passengerId == -1) {
            Toast.makeText(getContext(),
                    "User not logged in", Toast.LENGTH_SHORT).show();
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
            MaterialCardView card = (MaterialCardView) inflater.inflate(R.layout.view_admin_ride_card, cardsContainer, false);

            TextView tvRoute = card.findViewById(R.id.tvAdminRoute);
            TextView tvStart = card.findViewById(R.id.tvAdminStartAddress);
            TextView tvEnd = card.findViewById(R.id.tvAdminEndAddress);
            TextView tvDateTime = card.findViewById(R.id.tvAdminDateTime);
            TextView tvPrice = card.findViewById(R.id.tvAdminPrice);
            TextView tvStatus = card.findViewById(R.id.tvAdminStatus);
            TextView tvPanic = card.findViewById(R.id.tvAdminPanic);

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
            if (ride.panic) {
                tvPanic.setVisibility(View.VISIBLE);
            } else {
                tvPanic.setVisibility(View.GONE);
            }

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

    private void applyFilters() {
        String searchText = etTextFilter.getText().toString();
        String dateFrom = etDateFrom.getText().toString();
        String dateTo = etDateTo.getText().toString();

        List<RideDetailsDTO> filteredRides = RideFilterHelper.filterRides(allRides, searchText, dateFrom, dateTo);
        populateRideCards(filteredRides);
    }

    private void openRideDetails(Long rideId) {
        PassengerRideDetailsFragment fragment = PassengerRideDetailsFragment.newInstance(Math.toIntExact(rideId));
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
