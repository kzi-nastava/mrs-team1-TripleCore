package com.example.taxiapp.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;

import model.ChangePricesRequest;
import model.VehiclePricesDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.AuthService;
import service.PricingService;

public class PricingFormFragment extends Fragment {

    private EditText etStandard, etVan, etLuxury;
    private Button btnReset, btnSave;

    private VehiclePricesDTO prices = new VehiclePricesDTO();
    private Long adminId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pricing_form, container, false);
        adminId = AuthService.getInstance().getLoggedInUserId(requireContext());

        etStandard = view.findViewById(R.id.standardInput);
        etVan = view.findViewById(R.id.vanInput);
        etLuxury = view.findViewById(R.id.luxuryInput);
        btnReset = view.findViewById(R.id.pricingResetButton);
        btnSave = view.findViewById(R.id.pricingSaveButton);

        fetchPricesFromBackend();

        btnReset.setOnClickListener(v -> populateForm(prices));
        btnSave.setOnClickListener(v -> saveChanges());



        return view;
    }

    private void populateForm(VehiclePricesDTO prices) {
        etStandard.setText(String.valueOf(prices.standard));
        etVan.setText(String.valueOf(prices.van));
        etLuxury.setText(String.valueOf(prices.luxury));
    }

    private void saveChanges() {
        double standard = Double.parseDouble(etStandard.getText().toString());
        double van = Double.parseDouble(etVan.getText().toString());
        double luxury = Double.parseDouble(etLuxury.getText().toString());

        ChangePricesRequest request = new ChangePricesRequest();
        request.adminId = adminId;
        request.prices = new VehiclePricesDTO();
        request.prices.standard = standard;
        request.prices.van = van;
        request.prices.luxury = luxury;

        PricingService.getInstance().changePrices(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    prices = request.prices;
                    Toast.makeText(
                            requireContext(),
                            "Prices saved",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            requireContext(),
                            "Failed saving prices",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void fetchPricesFromBackend() {
        PricingService.getInstance().getPrices().enqueue(new Callback<VehiclePricesDTO>() {
            @Override
            public void onResponse(Call<VehiclePricesDTO> call, Response<VehiclePricesDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    prices = response.body();
                    populateForm(prices);
                } else {
                    Toast.makeText(
                            requireContext(),
                            "Failed fetching prices",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<VehiclePricesDTO> call, Throwable t) {
                Toast.makeText(
                        requireContext(),
                        "Failed fetching prices",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
