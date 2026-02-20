package com.example.taxiapp.ui.report;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.taxiapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.DailyReportDTO;
import model.SummaryDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.ReportService;

public class ReportFragment extends Fragment {

    private ImageView imgRidesChart, imgMoneyChart, imgDistanceChart;
    private TextView tvTotalRides, tvTotalDistance, tvTotalPrice, tvAvgDistance, tvAvgPrice;
    private LinearLayout summaryContainer;
    private Long userId;

    public ReportFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report, container, false);


        imgRidesChart = v.findViewById(R.id.imgRidesChart);
        imgMoneyChart = v.findViewById(R.id.imgMoneyChart);
        imgDistanceChart = v.findViewById(R.id.imgDistanceChart);

        tvTotalRides = v.findViewById(R.id.tvTotalRides);
        tvTotalDistance = v.findViewById(R.id.tvTotalDistance);
        tvTotalPrice = v.findViewById(R.id.tvTotalPrice);
        tvAvgDistance = v.findViewById(R.id.tvAvgDistance);
        tvAvgPrice = v.findViewById(R.id.tvAvgPrice);

        summaryContainer = v.findViewById(R.id.summaryContainer);
        Button btnPickDates = v.findViewById(R.id.btnPickDates);


        userId = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE).getLong("userId", -1);


        btnPickDates.setOnClickListener(view -> openDateRangePicker());

        return v;
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

            loadData(start, end);
        });
    }

    private void loadData(String from, String to) {
        ReportService.getInstance().getSummary(userId, from, to, new Callback<SummaryDTO>() {
            @Override
            public void onResponse(Call<SummaryDTO> call, Response<SummaryDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SummaryDTO s = response.body();
                    updateSummaryUI(s);
                    loadDailyCharts(from, to, s);
                } else {
                    String errorLog = "Unknown error";
                    try {
                        if (response.errorBody() != null) errorLog = response.errorBody().string();
                    } catch (IOException e) { e.printStackTrace(); }

                    Log.e("ReportService", "Code: " + response.code() + " Message: " + errorLog);
                    Toast.makeText(getContext(), "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SummaryDTO> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateSummaryUI(SummaryDTO s) {
        summaryContainer.setVisibility(View.VISIBLE);
        tvTotalRides.setText("Total rides: " + s.getTotalRides());
        tvTotalDistance.setText(String.format(Locale.getDefault(), "Total distance: %.2f m", s.getTotalDistance()));
        tvTotalPrice.setText(String.format(Locale.getDefault(), "Total price: %.2f RSD", s.getTotalPrice()));
        tvAvgDistance.setText(String.format(Locale.getDefault(), "Average distance: %.2f m", s.getAverageDistance()));
        tvAvgPrice.setText(String.format(Locale.getDefault(), "Average price: %.2f RSD", s.getAveragePrice()));
    }

    private void loadDailyCharts(String from, String to, SummaryDTO summary) {
        ReportService.getInstance().getDailyReport(userId, from, to, new Callback<List<DailyReportDTO>>() {
            @Override
            public void onResponse(Call<List<DailyReportDTO>> call, Response<List<DailyReportDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DailyReportDTO> data = response.body();


                    double calculatedAvgRides = 0;
                    if (!data.isEmpty()) {
                        calculatedAvgRides = (double) summary.getTotalRides() / data.size();
                    }


                    String ridesUrl = generateUrl(data, "Rides", "blue", "rides", calculatedAvgRides);
                    String moneyUrl = generateUrl(data, "Price", "green", "price", summary.getAveragePrice());
                    String distUrl = generateUrl(data, "Distance", "red", "distance", summary.getAverageDistance());

                    if (getContext() != null) {
                        Glide.with(ReportFragment.this).load(ridesUrl).into(imgRidesChart);
                        Glide.with(ReportFragment.this).load(moneyUrl).into(imgMoneyChart);
                        Glide.with(ReportFragment.this).load(distUrl).into(imgDistanceChart);
                    }
                }
            }

            @Override public void onFailure(Call<List<DailyReportDTO>> call, Throwable t) { }
        });
    }

    private String generateUrl(List<DailyReportDTO> data, String label, String color, String type, double avg) {
        if (data == null || data.isEmpty()) return "";

        StringBuilder labels = new StringBuilder();
        StringBuilder daily = new StringBuilder();
        StringBuilder cumulative = new StringBuilder();
        StringBuilder average = new StringBuilder();
        double runningSum = 0;

        for (int i = 0; i < data.size(); i++) {
            DailyReportDTO d = data.get(i);


            labels.append("'").append(d.getDate()).append("'");

            double val = type.equals("rides") ? d.getRideCount() :
                    (type.equals("price") ? d.getTotalPrice() : d.getTotalDistance());

            runningSum += val;


            daily.append(String.format(Locale.US, "%.2f", val));
            cumulative.append(String.format(Locale.US, "%.2f", runningSum));
            average.append(String.format(Locale.US, "%.2f", avg));

            if (i < data.size() - 1) {
                labels.append(",");
                daily.append(",");
                cumulative.append(",");
                average.append(",");
            }
        }


        String config = "{" +
                "type:'line'," +
                "data:{" +
                "labels:[" + labels + "]," +
                "datasets:[" +
                "{" +
                "label:'Daily'," +
                "data:[" + daily + "]," +
                "borderColor:'" + color + "'," +
                "fill:false," +
                "borderWidth:4," +
                "pointRadius:5" +
                "}," +
                "{" +
                "label:'Total'," +
                "data:[" + cumulative + "]," +
                "borderColor:'orange'," +
                "fill:false," +
                "borderDash:[5,5]," +
                "borderWidth:2" +
                "}," +
                "{" +
                "label:'Average'," +
                "data:[" + average + "]," +
                "borderColor:'purple'," +
                "fill:false," +
                "borderDash:[2,2]," +
                "borderWidth:2," +
                "pointRadius:0" +
                "}" +
                "]" +
                "}," +
                "options:{" +
                "scales:{" +
                "yAxes:[{ticks:{beginAtZero:true}}]," +
                "xAxes:[{ticks:{autoSkip:false, maxRotation:45, minRotation:45}}]" +
                "}," +
                "legend:{position:'bottom'}" +
                "}" +
                "}";

        return "https://quickchart.io/chart?c=" + config;
    }
}