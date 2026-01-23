package network;

import model.LoginRequest;
import model.LoginResponse;
import model.RegisterRequest;
import model.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
