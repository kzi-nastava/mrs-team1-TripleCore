package service;

import java.util.List;

import model.ActiveVehicleLocationResponse;
import model.RideTrackingInfo;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import service.api.ApiService;

public class VehicleService {
    private static VehicleService instance;

    private VehicleService() {}

    public static VehicleService getInstance() {
        if (instance == null) {
            instance = new VehicleService();
        }
        return instance;
    }

    public void getVehicleLocations(Long driverId, Callback<List<ActiveVehicleLocationResponse>> callback){
        ApiService api = RetrofitClient.getApiService();
        api.getVehicleLocations().enqueue(callback);
    }

    public void getRideTrackingInfo(Long rideId, Callback<RideTrackingInfo> callback){
        ApiService api = RetrofitClient.getApiService();
        api.getRideTrackingInfo(rideId).enqueue(callback);
    }
}
