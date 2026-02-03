package service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import model.RideDetailsDTO;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class PassengerService {

    private static PassengerService instance;

    private PassengerService() {}

    public static PassengerService getInstance() {
        if (instance == null) {
            instance = new PassengerService();
        }
        return instance;
    }

    public void getRideHistory(Long passengerId, Callback<List<RideDetailsDTO>> callback) {
        RetrofitClient.getApiService()
                .getPassengerRideHistory(passengerId)
                .enqueue(new Callback<List<RideDetailsDTO>>() {
                    @Override
                    public void onResponse(Call<List<RideDetailsDTO>> call, Response<List<RideDetailsDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onResponse(call, response);
                        } else {
                            Log.e("PassengerService", "getRideHistory failed: " + response.message());
                            callback.onFailure(call, new Throwable("Failed to get ride history"));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<RideDetailsDTO>> call, Throwable t) {
                        Log.e("PassengerService", "getRideHistory error", t);
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getRideDetails(Long passengerId, Long rideId, Callback<RideDetailsDTO> callback) {
        RetrofitClient.getApiService()
                .getPassengerRideDetails(passengerId, rideId)
                .enqueue(new Callback<RideDetailsDTO>() {
                    @Override
                    public void onResponse(Call<RideDetailsDTO> call, Response<RideDetailsDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onResponse(call, response);
                        } else {
                            Log.e("PassengerService", "getRideDetails failed: " + response.message());
                            callback.onFailure(call, new Throwable("Failed to get ride details"));
                        }
                    }

                    @Override
                    public void onFailure(Call<RideDetailsDTO> call, Throwable t) {
                        Log.e("PassengerService", "getRideDetails error", t);
                        callback.onFailure(call, t);
                    }
                });
    }
}
