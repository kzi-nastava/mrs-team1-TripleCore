package com.example.taxiapp.ui.shared;

import android.app.DatePickerDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import helper.RideFilterHelper;
import helper.ShakeDetector;
import model.RideDetailsDTO;

public class RideHistoryFragment extends Fragment {

    private static final String ARG_RIDE_HISTORY = "ride_history";
    private static final String ARG_SORT_DESC = "sort_desc";

    private List<RideDetailsDTO> rideHistory = new ArrayList<>();
    private boolean sortDescending = true;

    // ui elements
    private TextInputEditText etDateFrom;
    private TextInputEditText etDateTo;
    private TextInputEditText etTextFilter;
    private Button btnClear;
    private Button btnApply;
    private LinearLayout cardsContainer;

    // sensors
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeDetector shakeDetector;

    public RideHistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String json = getArguments().getString(ARG_RIDE_HISTORY);
            if (json != null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<RideDetailsDTO>>() {}.getType();
                rideHistory = gson.fromJson(json, listType);
            }
        }

        if (savedInstanceState != null) {
            sortDescending = savedInstanceState.getBoolean(ARG_SORT_DESC, true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_SORT_DESC, sortDescending);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_ride_history, container, false);

        initViews(view);
        initListeners(view);
        initSensors();

        loadRideCards(rideHistory);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(shakeDetector, accelerometer,
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(shakeDetector);
        }
    }

    private void initViews(View view) {
        etDateFrom = view.findViewById(R.id.etDateFrom);
        etDateTo = view.findViewById(R.id.etDateTo);
        etTextFilter = view.findViewById(R.id.etTextFilter);
        btnClear = view.findViewById(R.id.btnClear);
        btnApply = view.findViewById(R.id.btnApply);
        cardsContainer = view.findViewById(R.id.ride_history_cards_container);
    }

    private void initListeners(View view) {
        LinearLayout rootLayout = view.findViewById(R.id.root_layout);

        rootLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                etTextFilter.clearFocus();
                hideKeyboard(v);
            }
            return false;
        });

        etDateFrom.setOnClickListener(v -> showDatePicker(etDateFrom));
        etDateTo.setOnClickListener(v -> showDatePicker(etDateTo));
        btnClear.setOnClickListener(v -> clearInputs());
        btnApply.setOnClickListener(v -> applyFiltersAndSort());
    }

    private void hideKeyboard(View view) {
        if (getActivity() != null) {
            InputMethodManager imm =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void initSensors() {
        sensorManager = (SensorManager) requireContext()
                .getSystemService(requireContext().SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(this::applyFiltersAndSort);
    }

    private void loadRideCards(List<RideDetailsDTO> rides) {
        if (cardsContainer == null) return;

        cardsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (RideDetailsDTO ride : rides) {
            MaterialCardView card = (MaterialCardView)
                    inflater.inflate(R.layout.view_passenger_ride_card, cardsContainer, false);

            bindRideData(card, ride);
            card.setOnClickListener(v -> openRideDetails(ride));

            cardsContainer.addView(card);
        }
    }

    private void bindRideData(MaterialCardView card, RideDetailsDTO ride) {
        TextView tvRoute = card.findViewById(R.id.tvPassengerRoute);
        TextView tvStart = card.findViewById(R.id.tvPassengerStartAddress);
        TextView tvEnd = card.findViewById(R.id.tvPassengerEndAddress);
        TextView tvDateTime = card.findViewById(R.id.tvPassengerDateTime);
        TextView tvPrice = card.findViewById(R.id.tvPassengerPrice);
        TextView tvStatus = card.findViewById(R.id.tvPassengerStatus);
        TextView tvPanic = card.findViewById(R.id.tvPassengerPanic);

        if (ride.startLocation != null && ride.endLocation != null) {
            tvRoute.setText(ride.startLocation.address + " → " + ride.endLocation.address);
            tvStart.setText(ride.startLocation.address);
            tvEnd.setText(ride.endLocation.address);
        }

        String start = ride.startTime != null ? ride.startTime : "N/A";
        String end = ride.endTime != null ? ride.endTime : "N/A";
        tvDateTime.setText(start + " - " + end);

        tvPrice.setText(ride.price + " RSD");

        if (ride.status != null) {
            tvStatus.setText(ride.status);
            tvStatus.setTextColor(getStatusColor(ride.status));
        }

        tvPanic.setVisibility(ride.panic ? View.VISIBLE : View.GONE);
    }

    private int getStatusColor(String status) {
        switch (status) {
            case "FINISHED":
            case "IN_PROGRESS":
            case "ACCEPTED":
                return ContextCompat.getColor(requireContext(), R.color.green);
            case "CANCELLED":
                return ContextCompat.getColor(requireContext(), R.color.red);
            default:
                return ContextCompat.getColor(requireContext(), R.color.black);
        }
    }

    private void applyFiltersAndSort() {
        if (!isAdded() || getActivity() == null) return;

        String searchText = etTextFilter.getText().toString();
        String dateFrom = etDateFrom.getText().toString();
        String dateTo = etDateTo.getText().toString();

        List<RideDetailsDTO> filtered =
                RideFilterHelper.filterAndSortRides(
                        rideHistory,
                        searchText,
                        dateFrom,
                        dateTo,
                        sortDescending
                );

        loadRideCards(filtered);

        Toast.makeText(
                requireContext(),
                sortDescending ? "Sorted by newest first" : "Sorted by oldest first",
                Toast.LENGTH_SHORT
        ).show();

        sortDescending = !sortDescending;
    }

    private void clearInputs() {
        if (etTextFilter != null) etTextFilter.setText("");
        if (etDateFrom != null) etDateFrom.setText("");
        if (etDateTo != null) etDateTo.setText("");
    }

    private void showDatePicker(TextInputEditText dateInput) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, day) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, day);

                    SimpleDateFormat sdf =
                            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    dateInput.setText(sdf.format(selected.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    private void openRideDetails(RideDetailsDTO rideDetails) {
        if (!isAdded() || getActivity() == null) return;

        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString("ride_details", gson.toJson(rideDetails));

        RideDetailsFragment fragment = new RideDetailsFragment();
        fragment.setArguments(args);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}