package service;

import android.util.Log;

import model.RideCancelRequest;
import model.RideDetailsDTO;
import model.RideRequest;
import model.StopRideRequest;
import model.StopRideResponse;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
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

    public void cancelRide(Long rideId, String cancelerType, String reason,
                           Callback<ResponseBody> callback) {
        ApiService api = RetrofitClient.getApiService();

        RideCancelRequest request = new RideCancelRequest(reason, cancelerType);

        api.cancelRide(rideId, request)
                .enqueue(callback);
    }

    public void stopRide(Long rideId,
                         StopRideRequest request,
                         Callback<StopRideResponse> callback) {

        ApiService api = RetrofitClient.getApiService();

        api.stopRide(rideId, request)
                .enqueue(callback);
    }

    public void getRideDetails(Long rideId, Callback<RideDetailsDTO> callback) {
        api.getRideDetails(rideId).enqueue(callback);
    }

    public void activatePanic(Long rideId,
                              Long userId,
                              Callback<ResponseBody> callback) {
        api.activatePanic(rideId, userId).enqueue(callback);
    }

    public void orderRide(String email, RideRequest request, Callback<Void> callback) {
        Log.d("RideService", "Ordering ride for email: " + email + " with request: " + request);
        api.orderRide(email, request).enqueue(callback);
    }

}
