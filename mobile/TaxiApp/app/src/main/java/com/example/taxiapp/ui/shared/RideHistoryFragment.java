package com.example.taxiapp.ui.shared;

import static helper.KeyboardHelper.hideKeyboard;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import helper.DatePickerHelper;
import helper.RideCardHelper;
import helper.RideFilterHelper;
import helper.ShakeDetector;
import model.RideDetailsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.AdminService;
import service.DriverService;
import service.PassengerService;

public class RideHistoryFragment extends Fragment {

    private static final String ARG_ROLE_TYPE = "role_type";
    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_RIDE_HISTORY = "ride_history";
    private static final String ARG_SORT_DESC = "sort_desc";
    private static final String ARG_LOADED = "loaded";

    public enum RoleType {
        DRIVER,
        PASSENGER,
        ADMIN
    }

    private List<RideDetailsDTO> rideHistory = new ArrayList<>();
    private boolean sortDescending = true;
    private boolean isDataLoaded = false;
    private String currentRoleType;
    private Long currentUserId;

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

    // Factory methods for creating fragment instances based on role
    public static RideHistoryFragment newInstanceForDriver(Long userId) {
        RideHistoryFragment fragment = new RideHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROLE_TYPE, RoleType.DRIVER.name());
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    public static RideHistoryFragment newInstanceForPassenger(Long userId) {
        RideHistoryFragment fragment = new RideHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROLE_TYPE, RoleType.PASSENGER.name());
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    public static RideHistoryFragment newInstanceForAdmin(Long userId) {
        RideHistoryFragment fragment = new RideHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROLE_TYPE, RoleType.ADMIN.name());
        args.putLong(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    public RideHistoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentRoleType = getArguments().getString(ARG_ROLE_TYPE);
            currentUserId = getArguments().getLong(ARG_USER_ID, -1);
        }

        if (savedInstanceState != null) {
            sortDescending = savedInstanceState.getBoolean(ARG_SORT_DESC, true);
            isDataLoaded = savedInstanceState.getBoolean(ARG_LOADED, false);

            String json = savedInstanceState.getString(ARG_RIDE_HISTORY);
            if (json != null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<RideDetailsDTO>>() {}.getType();
                rideHistory = gson.fromJson(json, listType);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_SORT_DESC, sortDescending);
        outState.putBoolean(ARG_LOADED, isDataLoaded);

        if (!rideHistory.isEmpty()) {
            Gson gson = new Gson();
            String json = gson.toJson(rideHistory);
            outState.putString(ARG_RIDE_HISTORY, json);
        }

        outState.putString(ARG_ROLE_TYPE, currentRoleType);
        outState.putLong(ARG_USER_ID, currentUserId);
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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isDataLoaded) {
            loadRideHistoryBasedOnRole();
        } else if (!rideHistory.isEmpty()) {
            loadRideCards(rideHistory);
        }
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

        etDateFrom.setOnClickListener(v ->
                DatePickerHelper.showDatePicker(requireContext(), etDateFrom));
        etDateTo.setOnClickListener(v ->
                DatePickerHelper.showDatePicker(requireContext(), etDateTo));
        btnClear.setOnClickListener(v -> clearInputs());
        btnApply.setOnClickListener(v -> applyFiltersAndSort());
    }

    private void initSensors() {
        sensorManager = (SensorManager) requireContext()
                .getSystemService(requireContext().SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector(this::applyFiltersAndSort);
    }

    private void loadRideHistoryBasedOnRole() {
        if (currentUserId == -1) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentRoleType == null) {
            Toast.makeText(requireContext(), "Role not specified", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (currentRoleType) {
            case "DRIVER":
                loadDriverRideHistory();
                break;
            case "PASSENGER":
                loadPassengerRideHistory();
                break;
            case "ADMIN":
                loadAdminRideHistory();
                break;
            default:
                Toast.makeText(requireContext(), "Unknown role: " + currentRoleType, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDriverRideHistory() {
        DriverService driverService = DriverService.getInstance();
        driverService.getDriverRideHistory(currentUserId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (!isAdded() || getActivity() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RideDetailsDTO>>() {}.getType();
                        rideHistory = gson.fromJson(json, listType);
                        isDataLoaded = true;

                        requireActivity().runOnUiThread(() -> loadRideCards(rideHistory));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(
                                requireContext(),
                                "Error parsing driver ride history",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                } else {
                    Toast.makeText(
                            requireContext(),
                            "Failed loading driver ride history",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (!isAdded() || getActivity() == null) return;

                Toast.makeText(
                        requireContext(),
                        "Failed server communication for driver",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void loadPassengerRideHistory() {
        PassengerService.getInstance().getRideHistory(currentUserId, new Callback<List<RideDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<RideDetailsDTO>> call, Response<List<RideDetailsDTO>> response) {
                if (!isAdded() || getActivity() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    rideHistory = response.body();
                    isDataLoaded = true;

                    requireActivity().runOnUiThread(() -> loadRideCards(rideHistory));
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch passenger rides", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RideDetailsDTO>> call, Throwable t) {
                if (!isAdded() || getActivity() == null) return;

                Toast.makeText(requireContext(), "Failed to fetch passenger rides: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAdminRideHistory() {
        AdminService.getInstance().getAllRides(new Callback<List<RideDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<RideDetailsDTO>> call, Response<List<RideDetailsDTO>> response) {
                if (!isAdded() || getActivity() == null) return;

                if (response.isSuccessful() && response.body() != null) {
                    rideHistory = response.body();
                    isDataLoaded = true;

                    requireActivity().runOnUiThread(() -> loadRideCards(rideHistory));
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch admin rides", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RideDetailsDTO>> call, Throwable t) {
                if (!isAdded() || getActivity() == null) return;

                Toast.makeText(requireContext(), "Failed to fetch admin rides: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRideCards(List<RideDetailsDTO> rides) {
        if (cardsContainer == null || !isAdded()) return;

        RideCardHelper.loadRideCards(
                cardsContainer,
                rides,
                LayoutInflater.from(requireContext()),
                this::openRideDetails
        );
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

        loadRideCards(rideHistory);
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