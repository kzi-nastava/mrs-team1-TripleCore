package service;

import model.LoginRequest;
import model.LoginResponse;
import network.RetrofitClient;
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
}
