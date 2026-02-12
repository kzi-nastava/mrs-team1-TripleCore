package com.example.taxiapp.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;

import model.VehiclePricesDTO;

public class PricingFormFragment extends Fragment {

    private EditText standardInput, vanInput, luxuryInput;
    private Button resetButton, saveButton;

    // Hardkodovani test podatak
    private VehiclePricesDTO prices;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pricing_form, container, false);

        standardInput = view.findViewById(R.id.standardInput);
        vanInput = view.findViewById(R.id.vanInput);
        luxuryInput = view.findViewById(R.id.luxuryInput);

        resetButton = view.findViewById(R.id.resetButton);
        saveButton = view.findViewById(R.id.saveButton);

        // Hardkodovani objekat
        prices = new VehiclePricesDTO();
        prices.standard = 1.50;
        prices.van = 2.00;
        prices.luxury = 3.50;

        // Postavi vrednosti u EditText-ove
        standardInput.setText(String.valueOf(prices.standard));
        vanInput.setText(String.valueOf(prices.van));
        luxuryInput.setText(String.valueOf(prices.luxury));

        // Reset dugme
        resetButton.setOnClickListener(v -> {
            standardInput.setText(String.valueOf(prices.standard));
            vanInput.setText(String.valueOf(prices.van));
            luxuryInput.setText(String.valueOf(prices.luxury));
        });

        // Save dugme (trenutno samo prikazuje vrednosti u logu, može da se pozove backend)
        saveButton.setOnClickListener(v -> {
            double standard = Double.parseDouble(standardInput.getText().toString());
            double van = Double.parseDouble(vanInput.getText().toString());
            double luxury = Double.parseDouble(luxuryInput.getText().toString());

            // Ovde možeš pozvati backend API da sačuva nove cene
            // Za test samo ažuriramo lokalni objekat
            prices.standard = standard;
            prices.van = van;
            prices.luxury = luxury;
        });

        return view;
    }

    private void loadPrices(){

    }
}
