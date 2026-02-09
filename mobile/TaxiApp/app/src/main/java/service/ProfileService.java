package service;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.DriverProfileChangeRequest;
import model.DriverProfileChangeRequestResponse;
import model.DriverProfileResponse;
import model.UpdateUserProfileRequest;
import model.UserProfileResponse;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileService {

    private static ProfileService instance;

    private ProfileService() {}

    public static ProfileService getInstance() {
        if (instance == null) {
            instance = new ProfileService();
        }
        return instance;
    }

    public void getUserProfile(Long userId, Callback<UserProfileResponse> callback) {
        RetrofitClient.getApiService().getUserProfile(userId).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, response);
                } else {
                    Log.e("ProfileService", "getUserProfile failed: " + response.message());
                    callback.onFailure(call, new Throwable("Failed to get user profile"));
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e("ProfileService", "getUserProfile error", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void getDriverProfile(Long driverId, Callback<DriverProfileResponse> callback) {
        RetrofitClient.getApiService().getDriverProfile(driverId).enqueue(new Callback<DriverProfileResponse>() {
            @Override
            public void onResponse(Call<DriverProfileResponse> call, Response<DriverProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, response);
                } else {
                    Log.e("ProfileService", "getDriverProfile failed: " + response.message());
                    callback.onFailure(call, new Throwable("Failed to get driver profile"));
                }
            }

            @Override
            public void onFailure(Call<DriverProfileResponse> call, Throwable t) {
                Log.e("ProfileService", "getDriverProfile error", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void updateUserProfile(Long userId,
                                  UpdateUserProfileRequest request,
                                  Callback<Void> callback) {

        RetrofitClient.getApiService()
                .updateUserProfile(userId, request)
                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onResponse(call, response);
                        } else {
                            callback.onFailure(call,
                                    new Throwable("Update profile failed"));
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }


    public void submitDriverProfileChange(Long driverId,
                                          UpdateUserProfileRequest request,
                                          Callback<DriverProfileChangeRequest> callback) {
        RetrofitClient.getApiService()
                .submitDriverProfileChange(driverId, request)
                .enqueue(callback);
    }

    public void getAllDriverProfileRequests(Callback<List<DriverProfileChangeRequestResponse>> callback) {
        RetrofitClient.getApiService()
                .getAllDriverProfileRequests()
                .enqueue(new Callback<List<DriverProfileChangeRequestResponse>>() {
                    @Override
                    public void onResponse(Call<List<DriverProfileChangeRequestResponse>> call, Response<List<DriverProfileChangeRequestResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            callback.onResponse(call, Response.success(response.body()));
                        } else {
                            callback.onFailure(call, new Throwable("Failed to load driver requests: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DriverProfileChangeRequestResponse>> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void updateDriverProfileRequestStatus(Long requestId, String status, Callback<Void> callback) {
        RetrofitClient.getApiService().updateDriverProfileRequestStatus(requestId, status).enqueue(callback);
    }


    public void approveDriverRequest(Long requestId, Callback<String> callback) {
        RetrofitClient.getApiService()
                .approveDriverRequest(requestId)
                .enqueue(callback);
    }

    public void rejectDriverRequest(Long requestId, Callback<String> callback) {
        RetrofitClient.getApiService()
                .rejectDriverRequest(requestId)
                .enqueue(callback);
    }

}



