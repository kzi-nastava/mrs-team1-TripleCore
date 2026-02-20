package service;

import android.util.Log;

import model.RideDetailsDTO;
import model.UserBlockedResponse;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class AdminService {

    private static AdminService instance;

    private AdminService() {}

    public static AdminService getInstance() {
        if (instance == null) {
            instance = new AdminService();
        }
        return instance;
    }

    public void getAllRides(Callback<List<RideDetailsDTO>> callback) {
        RetrofitClient.getApiService().getAllRides().enqueue(new Callback<List<RideDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<RideDetailsDTO>> call, Response<List<RideDetailsDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, response);
                } else {
                    Log.e("AdminService", "getAllRides failed: " + response.message());
                    callback.onFailure(call, new Throwable("Failed to get all rides"));
                }
            }

            @Override
            public void onFailure(Call<List<RideDetailsDTO>> call, Throwable t) {
                Log.e("AdminService", "getAllRides error", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void getRideById(Long rideId, Callback<RideDetailsDTO> callback) {
        RetrofitClient.getApiService().getRideById(rideId).enqueue(new Callback<RideDetailsDTO>() {
            @Override
            public void onResponse(Call<RideDetailsDTO> call, Response<RideDetailsDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, response);
                } else {
                    Log.e("AdminService", "getRideById failed: " + response.message());
                    callback.onFailure(call, new Throwable("Failed to get ride details"));
                }
            }

            @Override
            public void onFailure(Call<RideDetailsDTO> call, Throwable t) {
                Log.e("AdminService", "getRideById error", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void getNonAdminUsers(Callback<List<UserBlockedResponse>> callback) {
        RetrofitClient.getApiService().getNonAdminUsers().enqueue(callback);
    }


    public void blockUser(Long userId, String note, Callback<UserBlockedResponse> callback) {
        RetrofitClient.getApiService().blockUser(userId, note).enqueue(callback);
    }

}
