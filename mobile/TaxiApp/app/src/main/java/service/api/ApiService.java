package service.api;

import java.util.List;

import model.ChangePricesRequest;
import model.ChatResponse;
import model.CreateReviewRequest;
import model.DriverProfileChangeRequest;
import model.DriverProfileChangeRequestResponse;
import model.DriverProfileResponse;
import model.ActiveVehicleLocationResponse;
import model.LoginRequest;
import model.LoginResponse;
import model.NotificationResponse;
import model.Panic;
import model.RegisterDriverRequest;
import model.RegisterRequest;
import model.RegisterResponse;
import model.ReviewDTO;
import model.RideCancelRequest;
import model.RideDetailsDTO;
import model.SaveAdminMessageRequest;
import model.SaveUserMessageRequest;
import model.StopRideRequest;
import model.StopRideResponse;
import model.RideTrackingInfo;
import model.UpdateUserProfileRequest;
import model.UserProfileResponse;
import model.VehiclePricesDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    // Admin - Panic notifications
    @GET("/api/admin/panics")
    Call<List<Panic>> getAllPanics();

    @GET("/api/admin/panics/active")
    Call<List<Panic>> getActivePanics();

    @GET("/api/admin/panics/resolved")
    Call<List<Panic>> getResolvedPanics();

    @PUT("/api/admin/panics/{id}/resolve")
    Call<Void> resolvePanic(@Path("id") Long panicId);

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

    // Ride actions

    @GET("api/rides/ride-details/{id}")
    Call<RideDetailsDTO> getRideDetails(@Path("id") Long rideId);

    @POST("/api/rides/{id}/cancel")
    Call<ResponseBody> cancelRide(
            @Path("id") Long rideId,
            @Body RideCancelRequest request
    );

    @POST("api/rides/{id}/stop")
    Call<StopRideResponse> stopRide(
            @Path("id") Long rideId,
            @Body StopRideRequest request
    );

    @POST("/api/rides/{id}/panic")
    Call<ResponseBody> activatePanic(
            @Path("id") Long rideId,
            @Query("userId") Long userId
    );

    // Profile
    @GET("/api/profile/user")
    Call<UserProfileResponse> getUserProfile(@Query("userId") Long userId);

    @GET("/api/profile/driver")
    Call<DriverProfileResponse> getDriverProfile(@Query("driverId") Long driverId);

    @PUT("/api/profile")
    Call<Void> updateUserProfile(@Query("userId") Long userId, @Body UpdateUserProfileRequest request);

    @POST("/api/profile/driver/{driverId}/change-request")
    Call<DriverProfileChangeRequest> submitDriverProfileChange(
            @Path("driverId") Long driverId,
            @Body UpdateUserProfileRequest request
    );

    @GET("/api/admin/driver-profile-requests")
    Call<List<DriverProfileChangeRequestResponse>> getAllDriverProfileRequests();

    @PUT("/api/admin/driver-requests/{id}/status")
    Call<Void> updateDriverProfileRequestStatus(@Path("id") Long requestId, @Query("status") String status);

    // Approve
    @PUT("/api/admin/driver-profile-requests/{id}/approve")
    Call<String> approveDriverRequest(@Path("id") Long requestId);

    // Reject
    @PUT("/api/admin/driver-profile-requests/{id}/reject")
    Call<String> rejectDriverRequest(@Path("id") Long requestId);

    @GET("api/drivers/{id}/ride-history")
    Call<ResponseBody> getDriverRideHistory(@Path("id") Long driverId);

    // Ride tracking
    @GET("api/vehicles/active-ride/{id}")
    Call<RideTrackingInfo> getRideTrackingInfo(@Path("id") Long rideId);

    @POST("/api/rides/{id}/finish")
    Call<String> finishRide(@Path("id") Long rideId);

    // Reviews
    @POST("api/reviews/create")
    Call<ResponseBody> createReview(@Body CreateReviewRequest request);

    @GET("api/reviews/passenger/{id}")
    Call<List<ReviewDTO>> getPassengerReviews(@Path("id") Long passengerId);

    @GET("api/reviews/driver/{id}")
    Call<List<ReviewDTO>> getDriverReviews(@Path("id") Long driverId);

    // Chat
    @GET("api/chats/all")
    Call<List<ChatResponse>> getChatList();

    @POST("api/chats/save-user-message")
    Call<Void> saveUserMessage(@Body SaveUserMessageRequest request);

    @POST("api/chats/save-admin-message")
    Call<Void> saveAdminMessage(@Body SaveAdminMessageRequest request);

    @GET("api/chats/user/{id}")
    Call<ChatResponse> getUserChat(@Path("id") Long userId);

    // Pricing

    @GET("api/prices/get")
    Call<VehiclePricesDTO> getPrices();

    @POST("api/prices/change")
    Call<ResponseBody> changePrices(@Body ChangePricesRequest request);

    // Passenger Notifications

    @GET("api/notifications/passenger/{id}")
    Call<List<NotificationResponse>> getPassengerNotifications(@Path("id") Long passengerId);

    @POST("api/notifications/{id}/mark-seen")
    Call<ResponseBody> markNotificationSeen(@Path("id") Long notificationId);

    // driver registration
    @POST("/api/driver-auth/register-driver")
    Call<ResponseBody> registerDriver(@Body RegisterDriverRequest request);


}
