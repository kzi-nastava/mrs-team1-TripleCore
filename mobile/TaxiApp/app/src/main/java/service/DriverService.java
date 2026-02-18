package service;

import static android.content.ContentValues.TAG;

import android.util.Log;

import model.RegisterDriverRequest;
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

    public void getDriverRideHistory(Long driverId, Callback<ResponseBody> callback){
        ApiService api = RetrofitClient.getApiService();
        api.getDriverRideHistory(driverId).enqueue(callback);
    }


    public void registerDriver(RegisterDriverRequest request, Callback<ResponseBody> callback) {
        Log.d("DriverService", "Attempting to register driver: " + request.getEmail());
        RetrofitClient.getApiService().registerDriver(request).enqueue(callback);
    }


}