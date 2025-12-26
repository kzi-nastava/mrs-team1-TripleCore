package com.example.taxiapp.ui.shared;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.taxiapp.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RideHistoryFragment extends Fragment {

    private TextInputEditText etDateFrom;
    private TextInputEditText etDateTo;
    private TextInputEditText etTextFilter;

    private Button applyBtn;
    private MaterialCardView selectedCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ride_history, container, false);

        // Dohvati referencu na prvi date picker
        etDateFrom = view.findViewById(R.id.etDateFrom);
        etDateTo = view.findViewById(R.id.etDateTo);
        etTextFilter = view.findViewById(R.id.etTextFilter);
        Button clearBtn = view.findViewById(R.id.btnClear);
        applyBtn = view.findViewById(R.id.btnApply);
        selectedCard = view.findViewById(R.id.card1);

        // Postavi klik listener da prikaže DatePickerDialog
        etDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(etDateFrom);
            }
        });

        etDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(etDateTo);
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputs();
            }
        });

        selectedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRideDetails();
            }
        });

        return view;
    }

    private void showDatePicker(TextInputEditText dateInput) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        dateInput.setText(sdf.format(selectedDate.getTime()));
                    }
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

    private void openRideDetails() {
        RideDetailsFragment fragment = new RideDetailsFragment();

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
