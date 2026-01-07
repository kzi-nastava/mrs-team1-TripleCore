package com.example.taxiapp.ui.admin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AdminRideHistoryFragment extends Fragment {

    private TextInputEditText etDateFrom;
    private TextInputEditText etDateTo;
    private TextInputEditText etTextFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_ride_history, container, false);

        etDateFrom = view.findViewById(R.id.etDateFrom);
        etDateTo = view.findViewById(R.id.etDateTo);
        etTextFilter = view.findViewById(R.id.etTextFilter);
        Button clearBtn = view.findViewById(R.id.btnClear);
        Button applyBtn = view.findViewById(R.id.btnApply);

        MaterialCardView card1 = view.findViewById(R.id.admin_card1);
        MaterialCardView card2 = view.findViewById(R.id.admin_card2);
        MaterialCardView card3 = view.findViewById(R.id.admin_card3);

        etDateFrom.setOnClickListener(v -> showDatePicker(etDateFrom));
        etDateTo.setOnClickListener(v -> showDatePicker(etDateTo));

        clearBtn.setOnClickListener(v -> clearInputs());

        applyBtn.setOnClickListener(v -> {

        });

        setupAdminCard1(card1);
        setupAdminCard2(card2);
        setupAdminCard3(card3);

        card1.setOnClickListener(v -> openRideDetails(1));
        card2.setOnClickListener(v -> openRideDetails(2));
        card3.setOnClickListener(v -> openRideDetails(3));

        return view;
    }

    private void setupAdminCard1(MaterialCardView card) {
        if (card == null) return;

        View cardView = card.getChildAt(0);
        if (cardView instanceof ViewGroup) {
            TextView tvRoute = cardView.findViewById(R.id.tvAdminRoute);
            TextView tvDateTime = cardView.findViewById(R.id.tvAdminDateTime);
            TextView tvPrice = cardView.findViewById(R.id.tvAdminPrice);
            TextView tvStatus = cardView.findViewById(R.id.tvAdminStatus);
            TextView tvPanic = cardView.findViewById(R.id.tvAdminPanic);

            if (tvRoute != null) tvRoute.setText("Novi Sad → Beograd");
            if (tvDateTime != null) tvDateTime.setText("01.12.2025 10:00 - 11:15");
            if (tvPrice != null) tvPrice.setText("2500 €");
            if (tvStatus != null) {
                tvStatus.setText("Completed");
                tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
            }
            if (tvPanic != null) tvPanic.setVisibility(View.GONE);
        }
    }

    private void setupAdminCard2(MaterialCardView card) {
        if (card == null) return;

        View cardView = card.getChildAt(0);
        if (cardView instanceof ViewGroup) {
            TextView tvRoute = cardView.findViewById(R.id.tvAdminRoute);
            TextView tvDateTime = cardView.findViewById(R.id.tvAdminDateTime);
            TextView tvPrice = cardView.findViewById(R.id.tvAdminPrice);
            TextView tvStatus = cardView.findViewById(R.id.tvAdminStatus);
            TextView tvPanic = cardView.findViewById(R.id.tvAdminPanic);
            TextView tvCancelledBy = cardView.findViewById(R.id.tvAdminCancelledBy);

            if (tvRoute != null) tvRoute.setText("Beograd → Niš");
            if (tvDateTime != null) tvDateTime.setText("03.12.2025 08:00 - 08:30");
            if (tvPrice != null) tvPrice.setText("0 €");
            if (tvStatus != null) {
                tvStatus.setText("Cancelled");
                tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red));
            }
            if (tvPanic != null) {
                tvPanic.setVisibility(View.VISIBLE);
                tvPanic.setBackgroundResource(R.drawable.bg_red_badge);
            }
            if (tvCancelledBy != null) {
                tvCancelledBy.setVisibility(View.VISIBLE);
                tvCancelledBy.setText("Cancelled by: DRIVER");
            }
        }
    }

    private void setupAdminCard3(MaterialCardView card) {
        if (card == null) return;

        View cardView = card.getChildAt(0);
        if (cardView instanceof ViewGroup) {
            TextView tvRoute = cardView.findViewById(R.id.tvAdminRoute);
            TextView tvDateTime = cardView.findViewById(R.id.tvAdminDateTime);
            TextView tvPrice = cardView.findViewById(R.id.tvAdminPrice);
            TextView tvStatus = cardView.findViewById(R.id.tvAdminStatus);
            TextView tvPanic = cardView.findViewById(R.id.tvAdminPanic);

            if (tvRoute != null) tvRoute.setText("Subotica → Novi Sad");
            if (tvDateTime != null) tvDateTime.setText("02.12.2025 12:00 - 13:30");
            if (tvPrice != null) tvPrice.setText("1800 €");
            if (tvStatus != null) {
                tvStatus.setText("Completed");
                tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
            }
            if (tvPanic != null) tvPanic.setVisibility(View.GONE);
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
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void clearInputs(){
        etTextFilter.setText("");
        etDateFrom.setText("");
        etDateTo.setText("");
    }

    private void openRideDetails(int rideId) {
        AdminRideDetailsFragment fragment = AdminRideDetailsFragment.newInstance(rideId);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}