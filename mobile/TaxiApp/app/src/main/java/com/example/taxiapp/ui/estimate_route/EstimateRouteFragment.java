package com.example.taxiapp.ui.estimate_route;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstimateRouteFragment extends Fragment {

    private static final String TAG = "OSM";
    private AutoCompleteTextView startPointEditText, destinationPointEditText;
    private MaterialButton estimateButton;
    private OsmService osmService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estimate_route, container, false);

        startPointEditText = view.findViewById(R.id.startPointEditText);
        destinationPointEditText = view.findViewById(R.id.destinationPointEditText);
        estimateButton = view.findViewById(R.id.estimateButton);

        setupRetrofit();
        setupAutoComplete(startPointEditText);
        setupAutoComplete(destinationPointEditText);

        estimateButton.setOnClickListener(v -> {
            String start = startPointEditText.getText().toString();
            String dest = destinationPointEditText.getText().toString();

            if (start.isEmpty() || dest.isEmpty()) {
                Toast.makeText(getContext(), "Please fill both fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Route estimated from " + start + " to " + dest, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Start: " + start + ", Destination: " + dest);

                startPointEditText.setText("");
                destinationPointEditText.setText("");
            }
        });

        return view;
    }

    private void setupRetrofit() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .header("User-Agent", "TaxiApp/1.0 (Android)")
                            .build();
                    return chain.proceed(request);
                })
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://nominatim.openstreetmap.org/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        osmService = retrofit.create(OsmService.class);
    }

    private void setupAutoComplete(AutoCompleteTextView editText) {
        editText.setThreshold(3); // Start suggesting after 3 characters

        editText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    searchPlaces(s.toString(), editText);
                }
            }
        });
    }

    private void searchPlaces(String query, AutoCompleteTextView editText) {
        osmService.search(query, "json", 10).enqueue(new Callback<List<OsmPlace>>() {
            @Override
            public void onResponse(Call<List<OsmPlace>> call, Response<List<OsmPlace>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OsmPlace> places = response.body();
                    List<String> placeNames = new ArrayList<>();
                    for (OsmPlace place : places) {
                        placeNames.add(place.getDisplayName());
                        Log.d(TAG, "Found place: " + place.getDisplayName() + " (" + place.getLat() + "," + place.getLon() + ")");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_dropdown_item_1line, placeNames);
                    editText.setAdapter(adapter);

                    editText.post(() -> {
                        if (editText.isFocused()) {
                            editText.showDropDown();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<OsmPlace>> call, Throwable t) {
                Log.e(TAG, "OSM search failed", t);
            }
        });
    }
}
