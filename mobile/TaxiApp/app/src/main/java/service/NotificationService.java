package service;


import java.util.List;

import model.NotificationResponse;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import service.api.ApiService;

public class NotificationService {

    private static NotificationService instance;
    private final ApiService apiService;

    private NotificationService() {
        apiService = RetrofitClient.getApiService();
    }

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    public void getPassengerNotifications(Long passengerId,
                                          Callback<List<NotificationResponse>> callback) {

        Call<List<NotificationResponse>> call =
                apiService.getPassengerNotifications(passengerId);

        call.enqueue(callback);
    }

    public void markNotificationSeen(Long notificationId,
                                     Callback<ResponseBody> callback) {

        Call<ResponseBody> call =
                apiService.markNotificationSeen(notificationId);

        call.enqueue(callback);
    }
}
