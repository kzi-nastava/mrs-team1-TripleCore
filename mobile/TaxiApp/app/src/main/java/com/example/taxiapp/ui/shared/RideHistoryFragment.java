package com.example.taxiapp.ui.shared;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.app.DatePickerDialog;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class RideHistoryFragment extends Fragment {

    // data
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

    // constructor
    public RideHistoryFragment(List<RideDetailsDTO> rideHistory) {
        this.rideHistory = rideHistory;
    }

    // init
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

    private void initSensors() {
        sensorManager = (SensorManager) requireContext()
                .getSystemService(requireContext().SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(this::applyFiltersAndSort);
    }

    // ui
    private void loadRideCards(List<RideDetailsDTO> rides) {
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
        etTextFilter.setText("");
        etDateFrom.setText("");
        etDateTo.setText("");
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

    // navigation
    private void openRideDetails(RideDetailsDTO rideDetails) {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new RideDetailsFragment(rideDetails))
                .addToBackStack(null)
                .commit();
    }
}
