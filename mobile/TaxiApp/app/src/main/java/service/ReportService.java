package service;

import android.util.Log;
import model.DailyReportDTO;
import model.SummaryDTO;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class ReportService {

    private static ReportService instance;

    private ReportService() {}

    public static ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }


    public void getDailyReport(Long userId, String from, String to, Callback<List<DailyReportDTO>> callback) {
        RetrofitClient.getApiService().getDailyReport(userId, from, to).enqueue(new Callback<List<DailyReportDTO>>() {
            @Override
            public void onResponse(Call<List<DailyReportDTO>> call, Response<List<DailyReportDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, response);
                } else {
                    Log.e("ReportService", "getDailyReport failed: " + response.message());
                    callback.onFailure(call, new Throwable("Failed to load daily report"));
                }
            }

            @Override
            public void onFailure(Call<List<DailyReportDTO>> call, Throwable t) {
                Log.e("ReportService", "getDailyReport error", t);
                callback.onFailure(call, t);
            }
        });
    }


    public void getSummary(Long userId, String from, String to, Callback<SummaryDTO> callback) {
        RetrofitClient.getApiService().getSummary(userId, from, to).enqueue(new Callback<SummaryDTO>() {
            @Override
            public void onResponse(Call<SummaryDTO> call, Response<SummaryDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, response);
                } else {

                    String errorMsg = "Unknown error";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (java.io.IOException e) {
                        Log.e("ReportService", "Error reading error body", e);
                    }

                    Log.e("ReportService", "Code: " + response.code());
                    Log.e("ReportService", "Server message: " + errorMsg);

                    callback.onFailure(call, new Throwable("Server responded with code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<SummaryDTO> call, Throwable t) {
                Log.e("ReportService", "getSummary error", t);
                callback.onFailure(call, t);
            }
        });
    }
}