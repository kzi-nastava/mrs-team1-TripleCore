package com.example.taxiapp.ui.order_ride;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.estimate_route.OsmPlace;
import com.example.taxiapp.ui.estimate_route.OsmService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import model.UserBlockedResponse;
import network.RetrofitClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderRideFragment extends Fragment {



    private static final String TAG = "OrderRide";


    private boolean isSelecting = false;

    private boolean isUserBlocked = false;
    private AutoCompleteTextView etStartPoint, etDestinationPoint;
    private EditText etStartTime;
    private LinearLayout containerStations, containerPassengers;
    private RadioGroup rgVehicleType;
    private CheckBox cbBaby, cbPets;
    private MaterialCardView layoutBlocked;
    private TextView tvBlockedNote;

    private OsmPlace startLocation = null;
    private OsmPlace destinationLocation = null;

    private OsmService osmService;
    private List<View> stationViews = new ArrayList<>();
    private List<View> passengerViews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_ride, container, false);
        initViews(view);
        setupRetrofit();

        setupLocationField(etStartPoint, true);
        setupLocationField(etDestinationPoint, false);

        checkUserStatus();

        return view;
    }

    private void initViews(View view) {
        etStartPoint = view.findViewById(R.id.etStartPoint);
        etDestinationPoint = view.findViewById(R.id.etDestinationPoint);
        etStartTime = view.findViewById(R.id.etStartTime);
        containerStations = view.findViewById(R.id.containerStations);
        containerPassengers = view.findViewById(R.id.containerPassengers);
        rgVehicleType = view.findViewById(R.id.rgVehicleType);
        cbBaby = view.findViewById(R.id.cbBaby);
        cbPets = view.findViewById(R.id.cbPets);
        layoutBlocked = view.findViewById(R.id.layoutBlocked);
        tvBlockedNote = view.findViewById(R.id.tvBlockedNote);

        view.findViewById(R.id.btnAddStation).setOnClickListener(v -> addStationRow());
        view.findViewById(R.id.btnAddPassenger).setOnClickListener(v -> addPassengerRow());
        view.findViewById(R.id.btnOrderRide).setOnClickListener(v -> orderRide());
    }

    private void checkUserStatus() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        long userId = prefs.getLong("userId", -1);
        Log.d(TAG, "Provera statusa za userId: " + userId);
        if (userId == -1) return;



        RetrofitClient.getApiService().getUserById(userId).enqueue(new Callback<UserBlockedResponse>() {
            @Override
            public void onResponse(Call<UserBlockedResponse> call, Response<UserBlockedResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isUserBlocked = response.body().isBlocked();

                    if (isUserBlocked) {

                        fetchBlockedNote(userId);
                    } else {
                        layoutBlocked.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call<UserBlockedResponse> call, Throwable t) {
                Log.e(TAG, "Status check failed");
            }
        });
    }

    private void fetchBlockedNote(long userId) {
        RetrofitClient.getApiService().getBlockedNote(userId).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                layoutBlocked.setVisibility(View.VISIBLE);
                if (response.isSuccessful() && response.body() != null) {
                    String note = response.body().get("note");
                    if (note != null && !note.isEmpty()) {
                        tvBlockedNote.setText("Blocked: " + note);
                    } else {
                        tvBlockedNote.setText("Your account is blocked.");
                    }
                }
            }
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                layoutBlocked.setVisibility(View.VISIBLE);
                tvBlockedNote.setText("Your account is blocked.");
            }
        });
    }

    private void applyBlockedUI() {
        if (!isAdded()) return;


        layoutBlocked.setVisibility(View.VISIBLE);
        tvBlockedNote.setText("Your account is blocked. You cannot order new rides.");



        etStartPoint.setEnabled(false);
        etDestinationPoint.setEnabled(false);
    }

    private void setupLocationField(AutoCompleteTextView editText, boolean isStart) {
        editText.setThreshold(3);

        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isSelecting || editText.isPerformingCompletion()) return;

                if (editText.hasFocus() && s.length() >= 3) {

                    searchPlaces(s.toString(), editText);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        editText.setOnItemClickListener((parent, view, position, id) -> {
            isSelecting = true;
            OsmPlace selected = (OsmPlace) parent.getItemAtPosition(position);

            if (isStart) {
                startLocation = selected;
            } else {
                destinationLocation = selected;
            }

            editText.setText(selected.getDisplayName(), false);
            editText.dismissDropDown();


            editText.postDelayed(() -> isSelecting = false, 1000);

            Log.d(TAG, (isStart ? "Start" : "Dest") + " location set: " + selected.getDisplayName());
        });
    }

    private void searchPlaces(String query, AutoCompleteTextView editText) {
        osmService.search(query, "json", 10).enqueue(new Callback<List<OsmPlace>>() {
            @Override
            public void onResponse(Call<List<OsmPlace>> call, Response<List<OsmPlace>> response) {
                if (response.isSuccessful() && response.body() != null && isAdded()) {
                    ArrayAdapter<OsmPlace> adapter = new ArrayAdapter<>(requireContext(),
                            android.R.layout.simple_dropdown_item_1line, response.body());
                    editText.setAdapter(adapter);

                    if (editText.hasFocus()) {
                        editText.showDropDown();
                    }
                }
            }
            @Override public void onFailure(Call<List<OsmPlace>> call, Throwable t) {
                Log.e(TAG, "Search error: " + t.getMessage());
            }
        });
    }

    private void addStationRow() {
        View row = getLayoutInflater().inflate(R.layout.item_dynamic_input, containerStations, false);
        AutoCompleteTextView et = row.findViewById(R.id.etDynamicInput);
        et.setHint("Station point");
        et.setThreshold(3);

        et.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isSelecting || !et.hasFocus() || et.isPerformingCompletion()) return;
                et.setTag(null);
                if (s.length() >= 3) searchPlaces(s.toString(), et);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        et.setOnItemClickListener((parent, view, position, id) -> {
            isSelecting = true;
            OsmPlace selected = (OsmPlace) parent.getItemAtPosition(position);
            et.setTag(selected);
            et.setText(selected.getDisplayName(), false);
            et.dismissDropDown();
            et.postDelayed(() -> isSelecting = false, 1000);
        });

        row.findViewById(R.id.btnRemove).setOnClickListener(v -> {
            containerStations.removeView(row);
            stationViews.remove(row);
        });
        containerStations.addView(row);
        stationViews.add(row);
    }

    private void orderRide() {

        Log.d(TAG, "Start location state: " + (startLocation == null ? "NULL" : "OK"));
        Log.d(TAG, "Dest location state: " + (destinationLocation == null ? "NULL" : "OK"));

        if (startLocation == null || destinationLocation == null) {
            Toast.makeText(getContext(), "Please select points from the list.", Toast.LENGTH_SHORT).show();
            return;
        }


        String timeStr = etStartTime.getText().toString().trim();
        if (!timeStr.isEmpty()) {
            try {
                String[] parts = timeStr.split(":");
                if (parts.length != 2) throw new Exception();

                int hour = Integer.parseInt(parts[0]);
                int minute = Integer.parseInt(parts[1]);

                if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                    Toast.makeText(getContext(), "Invalid time values!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Calendar now = Calendar.getInstance();
                Calendar selected = Calendar.getInstance();
                selected.set(Calendar.HOUR_OF_DAY, hour);
                selected.set(Calendar.MINUTE, minute);
                selected.set(Calendar.SECOND, 0);


                if (selected.before(now)) {
                    Toast.makeText(getContext(), "Start time cannot be in the past!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "Time is valid: " + timeStr);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Invalid time format! Use HH:mm", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        model.RideRequest rideRequest = new model.RideRequest();

        rideRequest.setStartLocation(new model.LocationDTO(
                startLocation.getDisplayName(),
                Double.parseDouble(startLocation.getLat()),
                Double.parseDouble(startLocation.getLon())
        ));

        rideRequest.setEndLocation(new model.LocationDTO(
                destinationLocation.getDisplayName(),
                Double.parseDouble(destinationLocation.getLat()),
                Double.parseDouble(destinationLocation.getLon())
        ));


        List<model.LocationDTO> stops = new ArrayList<>();
        for (View v : stationViews) {
            AutoCompleteTextView et = v.findViewById(R.id.etDynamicInput);
            OsmPlace place = (OsmPlace) et.getTag();
            if (place != null) {
                stops.add(new model.LocationDTO(
                        place.getDisplayName(),
                        Double.parseDouble(place.getLat()),
                        Double.parseDouble(place.getLon())
                ));
            }
        }
        rideRequest.setIntermediateStops(stops);


        List<String> passengerEmails = new ArrayList<>();
        for (View v : passengerViews) {
            EditText et = v.findViewById(R.id.etDynamicInput);
            String email = et.getText().toString().trim();
            if (!email.isEmpty()) {
                passengerEmails.add(email);
            }
        }
        rideRequest.setLinkedPassengerEmails(passengerEmails);


        rideRequest.setBabyFriendly(cbBaby.isChecked());
        rideRequest.setPetFriendly(cbPets.isChecked());

        int selectedId = rgVehicleType.getCheckedRadioButtonId();
        if (selectedId == R.id.rbStandard) rideRequest.setVehicleType("STANDARD");
        else if (selectedId == R.id.rbLuxury) rideRequest.setVehicleType("LUXURY");
        else rideRequest.setVehicleType("VAN");

        String finalTimeStr = etStartTime.getText().toString().trim();
        if (!finalTimeStr.isEmpty()) {

            String currentDay = java.time.LocalDate.now().toString();
            String isoDateTime = currentDay + "T" + finalTimeStr + ":00";
            rideRequest.setStartTime(isoDateTime);
        } else {

            rideRequest.setStartTime(null);
        }

        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", "");

        Log.d("EMAIL_DEBUG", "Email from prefs: " + userEmail);

        service.RideService.getInstance().orderRide(userEmail, rideRequest, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Ride Ordered Successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "There is no avaliable driver for ride or your account is blocked", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addPassengerRow() {
        View row = getLayoutInflater().inflate(R.layout.item_dynamic_input, containerPassengers, false);
        EditText et = row.findViewById(R.id.etDynamicInput);
        et.setHint("Passenger email");
        et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        row.findViewById(R.id.btnRemove).setOnClickListener(v -> {
            containerPassengers.removeView(row);
            passengerViews.remove(row);
        });
        containerPassengers.addView(row);
        passengerViews.add(row);
    }

    private void setupRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("User-Agent", "TaxiApp/1.0").build();
                    return chain.proceed(request);
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        osmService = retrofit.create(OsmService.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated called. Checking for arguments...");
        Log.d(TAG, "Arguments: " + (getArguments() != null ? getArguments().toString() : "NULL"));
        if (getArguments() != null) {
            // --- START POINT ---
            String startAddr = getArguments().getString("startAddr");
            if (startAddr != null) {
                etStartPoint.setText(startAddr, false);
                startLocation = new OsmPlace();
                startLocation.setDisplayName(startAddr);
                startLocation.setLat(String.valueOf(getArguments().getDouble("startLat")));
                startLocation.setLon(String.valueOf(getArguments().getDouble("startLon")));
            }


            String destAddr = getArguments().getString("endAddr");
            if (destAddr != null) {
                etDestinationPoint.setText(destAddr, false);
                destinationLocation = new OsmPlace();
                destinationLocation.setDisplayName(destAddr);
                destinationLocation.setLat(String.valueOf(getArguments().getDouble("destLat")));
                destinationLocation.setLon(String.valueOf(getArguments().getDouble("destLon")));
            }

            Log.d(TAG, "Favoriti učitani. Start: " + startAddr + ", Dest: " + destAddr);
        }
    }
}