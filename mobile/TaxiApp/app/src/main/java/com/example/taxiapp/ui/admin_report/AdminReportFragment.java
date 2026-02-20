package com.example.taxiapp.ui.admin_report;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.taxiapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import model.DailyReportDTO;
import model.UserReportResponse;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.ReportService;

public class AdminReportFragment extends Fragment {

    private ImageView imgRidesChart, imgMoneyChart, imgDistanceChart;
    private TextView tvTotalRides, tvTotalDistance, tvTotalPrice, tvAvgDistance, tvAvgPrice;
    private View summaryContainer;
    private Spinner userSpinner;
    private List<UserReportResponse> userList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_report, container, false);

        imgRidesChart = v.findViewById(R.id.imgRidesChart);
        imgMoneyChart = v.findViewById(R.id.imgMoneyChart);
        imgDistanceChart = v.findViewById(R.id.imgDistanceChart);
        tvTotalRides = v.findViewById(R.id.tvTotalRides);
        tvTotalDistance = v.findViewById(R.id.tvTotalDistance);
        tvTotalPrice = v.findViewById(R.id.tvTotalPrice);
        tvAvgDistance = v.findViewById(R.id.tvAvgDistance);
        tvAvgPrice = v.findViewById(R.id.tvAvgPrice);
        summaryContainer = v.findViewById(R.id.summaryContainer);
        userSpinner = v.findViewById(R.id.userSpinner);
        Button btnPickDates = v.findViewById(R.id.btnPickDates);

        loadUserList();

        btnPickDates.setOnClickListener(view -> openDateRangePicker());

        return v;
    }

    private void loadUserList() {
        RetrofitClient.getApiService().getAllUsers().enqueue(new Callback<List<UserReportResponse>>() {
            @Override
            public void onResponse(Call<List<UserReportResponse>> call, Response<List<UserReportResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<UserReportResponse> allUsers = response.body();
                    userList = new ArrayList<>();
                    List<String> displayNames = new ArrayList<>();
                    displayNames.add("All users");


                    long currentAdminId = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getLong("userId", -1);

                    for (UserReportResponse u : allUsers) {

                        if (u.getId() != currentAdminId) {
                            userList.add(u);
                            displayNames.add(u.getEmail());
                        }
                    }

                    if (isAdded() && getContext() != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, displayNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        userSpinner.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<UserReportResponse>> call, Throwable t) {}
        });
    }

    private void openDateRangePicker() {
        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select date range")
                .build();

        picker.show(getChildFragmentManager(), "DATE_PICKER");

        picker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String start = sdf.format(new Date(selection.first));
            String end = sdf.format(new Date(selection.second));

            int selectedPos = userSpinner.getSelectedItemPosition();
            if (selectedPos == 0) {
                loadAllUsersReport(start, end);
            } else {
                Long userId = userList.get(selectedPos - 1).getId();
                loadSingleUserReport(userId, start, end);
            }
        });
    }

    private void loadAllUsersReport(String start, String end) {
        RetrofitClient.getApiService().getReportForAllUsers(start, end).enqueue(new Callback<List<DailyReportDTO>>() {
            @Override
            public void onResponse(Call<List<DailyReportDTO>> call, Response<List<DailyReportDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processAdminData(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<DailyReportDTO>> call, Throwable t) {}
        });
    }

    private void loadSingleUserReport(Long userId, String start, String end) {
        ReportService.getInstance().getDailyReport(userId, start, end, new Callback<List<DailyReportDTO>>() {
            @Override
            public void onResponse(Call<List<DailyReportDTO>> call, Response<List<DailyReportDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processAdminData(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<DailyReportDTO>> call, Throwable t) {}
        });
    }

    private void processAdminData(List<DailyReportDTO> data) {
        if (data == null || data.isEmpty()) return;


        Map<String, DailyReportDTO> map = new TreeMap<>();
        for (DailyReportDTO d : data) {
            String date = d.getDate().trim();
            if (map.containsKey(date)) {
                DailyReportDTO existing = map.get(date);
                existing.setRideCount(existing.getRideCount() + d.getRideCount());
                existing.setTotalDistance(existing.getTotalDistance() + d.getTotalDistance());
                existing.setTotalPrice(existing.getTotalPrice() + d.getTotalPrice());
            } else {
                DailyReportDTO copy = new DailyReportDTO();
                copy.setDate(date);
                copy.setRideCount(d.getRideCount());
                copy.setTotalDistance(d.getTotalDistance());
                copy.setTotalPrice(d.getTotalPrice());
                map.put(date, copy);
            }
        }

        List<DailyReportDTO> cleanData = new ArrayList<>(map.values());


        int totalRides = 0;
        double totalDist = 0;
        double totalPrice = 0;
        for (DailyReportDTO d : cleanData) {
            totalRides += d.getRideCount();
            totalDist += d.getTotalDistance();
            totalPrice += d.getTotalPrice();
        }

        double avgDist = totalRides > 0 ? totalDist / totalRides : 0;
        double avgPrice = totalRides > 0 ? totalPrice / totalRides : 0;
        double avgRidesPerDay = (double) totalRides / cleanData.size();

        summaryContainer.setVisibility(View.VISIBLE);
        tvTotalRides.setText("Total rides: " + totalRides);
        tvTotalDistance.setText(String.format(Locale.US, "Total distance: %.2f m", totalDist));
        tvTotalPrice.setText(String.format(Locale.US, "Total price: %.2f RSD", totalPrice));
        tvAvgDistance.setText(String.format(Locale.US, "Avereage distance: %.2f m", avgDist));
        tvAvgPrice.setText(String.format(Locale.US, "Average price: %.2f RSD", avgPrice));


        Glide.with(this).load(generateUrl(cleanData, "Rides", "blue", "rides", avgRidesPerDay)).into(imgRidesChart);
        Glide.with(this).load(generateUrl(cleanData, "Price", "green", "price", avgPrice)).into(imgMoneyChart);
        Glide.with(this).load(generateUrl(cleanData, "Distance", "red", "distance", avgDist)).into(imgDistanceChart);
    }

    private String generateUrl(List<DailyReportDTO> data, String label, String color, String type, double avg) {
        if (data == null || data.isEmpty()) return "";


        int step = 1;
        if (data.size() > 25) step = 2;
        if (data.size() > 50) step = 4;

        StringBuilder labels = new StringBuilder();
        StringBuilder daily = new StringBuilder();
        StringBuilder total = new StringBuilder();
        StringBuilder average = new StringBuilder();
        double runningSum = 0;

        for (int i = 0; i < data.size(); i++) {
            DailyReportDTO d = data.get(i);
            double val = type.equals("rides") ? d.getRideCount() :
                    (type.equals("price") ? d.getTotalPrice() : d.getTotalDistance());
            runningSum += val;


            if (i % step == 0 || i == data.size() - 1) {
                labels.append("'").append(d.getDate().substring(5)).append("'");
                daily.append(String.format(Locale.US, "%.0f", val));
                total.append(String.format(Locale.US, "%.0f", runningSum));
                average.append(String.format(Locale.US, "%.0f", avg));

                if (i < data.size() - 1) {
                    labels.append(",");
                    daily.append(",");
                    total.append(",");
                    average.append(",");
                }
            }
        }


        String config = "{type:'line',data:{labels:[" + labels + "]," +
                "datasets:[" +
                "{label:'Daily',data:[" + daily + "],borderColor:'" + color + "',fill:false,borderWidth:2,pointRadius:1}," +
                "{label:'Total',data:[" + total + "],borderColor:'orange',fill:false,borderDash:[5,5],borderWidth:1}," +
                "{label:'Average',data:[" + average + "],borderColor:'purple',fill:false,borderDash:[2,2],borderWidth:1,pointRadius:0}" +
                "]},options:{scales:{yAxes:[{ticks:{beginAtZero:true}}],xAxes:[{ticks:{autoSkip:true}}]}}}";

        return "https://quickchart.io/chart?c=" + Uri.encode(config);
    }
}