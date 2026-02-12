package service;

import java.util.List;

import model.CreateReviewRequest;
import model.ReviewDTO;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import service.api.ApiService;

public class ReviewService {

    private final ApiService apiService;

    public ReviewService() {
        this.apiService = RetrofitClient.getApiService();
    }

    public void createReview(CreateReviewRequest request,
                             Callback<ResponseBody> callback) {
        apiService.createReview(request).enqueue(callback);
    }

    public void getPassengerReviews(Long passengerId,
                                    Callback<List<ReviewDTO>> callback) {
        apiService.getPassengerReviews(passengerId).enqueue(callback);
    }

    public void getDriverReviews(Long driverId,
                                 Callback<List<ReviewDTO>> callback) {
        apiService.getDriverReviews(driverId).enqueue(callback);
    }
}
