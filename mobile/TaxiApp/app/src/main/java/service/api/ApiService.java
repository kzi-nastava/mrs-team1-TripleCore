package service.api;

import java.util.List;

import model.LoginRequest;
import model.LoginResponse;
import model.RegisterRequest;
import model.RegisterResponse;
import model.RideDetailsDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Auth
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    // Admin

    @GET("/api/admin/rides")
    Call<List<RideDetailsDTO>> getAllRides();

    @GET("/api/admin/rides/{id}")
    Call<RideDetailsDTO> getRideById(@Path("id") Long rideId);

    // Passenger
    @GET("/api/passengers/{id}/ride-history")
    Call<List<RideDetailsDTO>> getPassengerRideHistory(@Path("id") Long passengerId);

    @GET("/api/passengers/{id}/ride-history/{rideId}")
    Call<RideDetailsDTO> getPassengerRideDetails(@Path("id") Long passengerId, @Path("rideId") Long rideId);

}
