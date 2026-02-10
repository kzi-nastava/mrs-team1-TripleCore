package service;

import model.RideCancelRequest;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import service.api.ApiService;

public class RideService {
    private static RideService instance;

    private RideService() {}

    public static RideService getInstance() {
        if (instance == null) {
            instance = new RideService();
        }
        return instance;
    }

    public void cancelRide(Long rideId, String cancelerType, String reason,
                           Callback<ResponseBody> callback) {
        ApiService api = RetrofitClient.getApiService();

        RideCancelRequest request = new RideCancelRequest(reason, cancelerType);

        api.cancelRide(rideId, request)
                .enqueue(callback);
    }

}