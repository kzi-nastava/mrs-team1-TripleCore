package service;

import model.LoginRequest;
import model.LoginResponse;
import model.RegisterRequest;
import model.RegisterResponse;
import network.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AuthService {

    private static AuthService instance;

    private AuthService() {}

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public void login(String email, String password, Callback<LoginResponse> callback) {
        LoginRequest request = new LoginRequest(email, password);
        RetrofitClient.getApiService().login(request).enqueue(callback);
    }

    public void register(RegisterRequest request,
                         Callback<RegisterResponse> callback) {

        RetrofitClient
                .getApiService().register(request).enqueue(callback);
    }

    public void forgotPassword(String email, Callback<ResponseBody> callback) {
        RetrofitClient
                .getApiService()
                .forgotPassword(email)
                .enqueue(callback);
    }

    public void resetPassword(Long userId,
                              String newPassword,
                              Callback<ResponseBody> callback) {
        RetrofitClient
                .getApiService()
                .resetPassword(userId, newPassword)
                .enqueue(callback);
    }
}
