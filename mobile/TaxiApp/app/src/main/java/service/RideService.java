package service;

import network.RetrofitClient;
import retrofit2.Callback;
import service.api.ApiService;

public class RideService {

    private static RideService instance;
    private final ApiService api;

    private RideService() {
        api = RetrofitClient.getApiService();
    }

    public static RideService getInstance() {
        if (instance == null) {
            instance = new RideService();
        }
        return instance;
    }

    public void finishRide(Long rideId, Callback<String> callback) {
        api.finishRide(rideId).enqueue(callback);
    }
}
