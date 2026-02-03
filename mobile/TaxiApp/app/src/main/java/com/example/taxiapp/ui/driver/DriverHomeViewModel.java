package com.example.taxiapp.ui.driver;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.AuthService;
import service.DriverService;

public class DriverHomeViewModel extends ViewModel {
    private static final String TAG = "DriverViewModel";

    private final MutableLiveData<Boolean> isActive = new MutableLiveData<>(true);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private DriverService driverService = DriverService.getInstance();
    private AuthService authService = AuthService.getInstance();
    private Long driverId = null;

    public LiveData<Boolean> getIsActive() {
        return isActive;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public boolean initialize(Context context) {
        if (!authService.isDriver(context)) {
            return false;
        }

        driverId = authService.getLoggedInUserId(context);

        if (driverId != -1L) {
            loadInitialStatus();
            return true;
        }

        return false;
    }

    private void loadInitialStatus() {
        if (driverId == null) return;

        driverService.getDriverAvailability(driverId, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    try {
                        String body = response.body().string().trim();

                        boolean isAvailable;
                        if ("true".equalsIgnoreCase(body)) {
                            isAvailable = true;
                        } else if ("false".equalsIgnoreCase(body)) {
                            isAvailable = false;
                        } else {
                            isAvailable = Boolean.parseBoolean(body);
                        }

                        isActive.setValue(isAvailable);

                    } catch (Exception e) {
                        isActive.setValue(true);
                    }
                } else {
                    isActive.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isActive.setValue(true);
            }
        });
    }

    public void toggleActive() {
        if (driverId == null) {
            errorMessage.setValue("Driver ID not found");
            return;
        }

        Boolean current = isActive.getValue();
        if (current == null) {
            return;
        }

        boolean newStatus = !current;

        isActive.setValue(newStatus);

        driverService.changeDriverAvailability(driverId, newStatus,
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            try {
                                String body = response.body().string();
                            } catch (Exception e) {
                                Log.e(TAG, "Error reading response body", e);
                            }
                        } else {
                            try {
                                String error = response.errorBody() != null ?
                                        response.errorBody().string() : "No error body";

                                if (error.toLowerCase().contains("active ride") ||
                                        error.toLowerCase().contains("ride in progress")) {
                                    errorMessage.setValue("Cannot change: " + error);
                                } else {
                                    errorMessage.setValue("Failed: " + error);
                                }
                            } catch (Exception e) {
                                errorMessage.setValue("Failed to update status");
                            }

                            isActive.setValue(!newStatus);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        errorMessage.setValue("Network error: " + t.getMessage());

                        isActive.setValue(!newStatus);
                    }
                });
    }

    public Long getDriverId() {
        return driverId;
    }
}