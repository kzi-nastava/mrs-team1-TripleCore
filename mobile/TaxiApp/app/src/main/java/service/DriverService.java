package service;

import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import service.api.ApiService;

public class DriverService {
    private static DriverService instance;

    private DriverService() {}

    public static DriverService getInstance() {
        if (instance == null) {
            instance = new DriverService();
        }
        return instance;
    }

    public void changeDriverAvailability(Long driverId, boolean available,
                                         Callback<ResponseBody> callback) {
        ApiService api = RetrofitClient.getApiService();
        api.changeDriverAvailability(driverId, available)
                .enqueue(callback);
    }

    public void getDriverAvailability(Long driverId,
                                      Callback<ResponseBody> callback) {
        ApiService api = RetrofitClient.getApiService();
        api.getDriverAvailability(driverId)
                .enqueue(callback);
    }
}