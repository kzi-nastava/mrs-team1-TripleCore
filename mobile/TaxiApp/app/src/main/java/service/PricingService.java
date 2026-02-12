package service;

import model.ChangePricesRequest;
import model.VehiclePricesDTO;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import service.api.ApiService;

public class PricingService {

    private static PricingService instance;
    private ApiService apiService;

    private PricingService() {
        apiService = RetrofitClient.getApiService();
    }

    public static PricingService getInstance() {
        if (instance == null) {
            instance = new PricingService();
        }
        return instance;
    }

    public Call<VehiclePricesDTO> getPrices() {
        return apiService.getPrices();
    }

    public Call<ResponseBody> changePrices(ChangePricesRequest request) {
        return apiService.changePrices(request);
    }
}