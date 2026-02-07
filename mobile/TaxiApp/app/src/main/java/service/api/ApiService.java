package service.api;

import java.util.List;

import model.ActiveVehicleLocationResponse;
import model.LoginRequest;
import model.LoginResponse;
import model.RegisterRequest;
import model.RegisterResponse;
import model.RideDetailsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Auth
    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("/api/auth/forgot-password")
    Call<ResponseBody> forgotPassword(@Query("email") String email);

    @POST("/api/auth/reset-password")
    Call<ResponseBody> resetPassword(
            @Query("userId") Long userId,
            @Query("newPassword") String newPassword
    );

    // Guest

    @GET("api/vehicles/locations")
    Call<List<ActiveVehicleLocationResponse>> getVehicleLocations();

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

    // Driver
    @PATCH("/api/drivers/{id}/availability")
    Call<ResponseBody> changeDriverAvailability(
            @Path("id") Long driverId,
            @Query("available") boolean available
    );

    @GET("api/drivers/{id}/availability")
    Call<ResponseBody> getDriverAvailability(@Path("id") Long driverId);

    @GET("api/drivers/{id}/ride-history")
    Call<ResponseBody> getDriverRideHistory(@Path("id") Long driverId);
}
