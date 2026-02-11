package helper;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import model.RideDetailsDTO;
import model.StopRideRequest;
import model.StopRideResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.RideService;

public class RideActionHelper {

    public interface CancelRideCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface StopRideCallback {
        void onSuccess(StopRideResponse response);
        void onFailure(String error);
    }

    // ========================= CANCEL RIDE =========================

    public static void showCancelDialog(Context context,
                                        RideDetailsDTO ride,
                                        String userRole,
                                        Long userId,
                                        CancelRideCallback callback) {

        if (ride == null || userRole == null || userId == -1) {
            if (callback != null) callback.onFailure("Invalid parameters");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cancel Ride");

        if ("DRIVER".equals(userRole)) {

            final EditText input = new EditText(context);
            input.setHint("Enter cancellation reason (required)");
            builder.setView(input);
            builder.setMessage("Driver must provide a cancellation reason:");

            builder.setPositiveButton("Cancel Ride", (dialog, which) -> {
                String reason = input.getText().toString().trim();

                if (reason.isEmpty()) {
                    Toast.makeText(context,
                            "Reason is required for driver",
                            Toast.LENGTH_SHORT).show();

                    showCancelDialog(context, ride, userRole, userId, callback);
                } else {
                    executeCancelRide(ride.id, userRole, reason, callback);
                }
            });

        } else if ("PASSENGER".equals(userRole)) {

            builder.setMessage("Are you sure you want to cancel this ride?");
            builder.setPositiveButton("Yes, Cancel", (dialog, which) ->
                    executeCancelRide(ride.id, userRole, "", callback)
            );

        } else {

            Toast.makeText(context,
                    "You don't have permission to cancel this ride",
                    Toast.LENGTH_SHORT).show();

            if (callback != null) callback.onFailure("Permission denied");
            return;
        }

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private static void executeCancelRide(Long rideId,
                                          String userRole,
                                          String reason,
                                          CancelRideCallback callback) {

        String cancelerType =
                "PASSENGER".equals(userRole) ? "PASSENGER" : "DRIVER";

        RideService.getInstance().cancelRide(
                rideId,
                cancelerType,
                reason,
                new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {

                        if (response.isSuccessful()) {
                            if (callback != null) callback.onSuccess();
                        } else {

                            String error = "Failed to cancel ride";

                            try {
                                if (response.errorBody() != null) {
                                    String errorBody =
                                            response.errorBody().string();

                                    if (errorBody.contains("Passenger can only cancel")) {
                                        error = "You can only cancel up to 10 minutes before start";
                                    } else if (errorBody.contains("Driver must provide")) {
                                        error = "Driver must provide a cancellation reason";
                                    } else if (errorBody.contains("not found")) {
                                        error = "Ride not found";
                                    }
                                }
                            } catch (Exception ignored) {}

                            if (callback != null)
                                callback.onFailure(error);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call,
                                          Throwable t) {
                        if (callback != null)
                            callback.onFailure("Network error: " + t.getMessage());
                    }
                }
        );
    }

    // ========================= STOP RIDE =========================

    public static void showStopDialog(Context context,
                                      RideDetailsDTO ride,
                                      StopRideCallback callback) {

        if (ride == null) {
            if (callback != null) callback.onFailure("Invalid ride");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Stop Ride");
        builder.setMessage("Are you sure you want to stop this ride?");

        builder.setPositiveButton("Yes, Stop", (dialog, which) -> {

            dialog.dismiss();

            Toast.makeText(context,
                    "Getting current location...",
                    Toast.LENGTH_SHORT).show();

            LocationHelper.getCurrentLocation(context,
                    new LocationHelper.LocationCallback() {

                        @Override
                        public void onLocationReceived(double lat,
                                                       double lon) {

                            String address =
                                    ride.endLocation != null &&
                                            ride.endLocation.address != null
                                            ? ride.endLocation.address
                                            : "Unknown location";

                            StopRideRequest request =
                                    new StopRideRequest(lat, lon, address);

                            RideService.getInstance().stopRide(
                                    ride.id,
                                    request,
                                    new Callback<StopRideResponse>() {

                                        @Override
                                        public void onResponse(
                                                Call<StopRideResponse> call,
                                                Response<StopRideResponse> response) {

                                            if (response.isSuccessful()
                                                    && response.body() != null) {

                                                if (callback != null)
                                                    callback.onSuccess(response.body());

                                            } else {

                                                String error = "Failed to stop ride";

                                                try {
                                                    if (response.errorBody() != null) {
                                                        String errorBody =
                                                                response.errorBody().string();

                                                        if (errorBody.contains("not in progress")) {
                                                            error = "Ride must be in progress";
                                                        } else if (errorBody.contains("not found")) {
                                                            error = "Ride not found";
                                                        }
                                                    }
                                                } catch (Exception ignored) {}

                                                if (callback != null)
                                                    callback.onFailure(error);
                                            }
                                        }

                                        @Override
                                        public void onFailure(
                                                Call<StopRideResponse> call,
                                                Throwable t) {

                                            if (callback != null)
                                                callback.onFailure("Network error: " + t.getMessage());
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onFailure(String error) {
                            if (callback != null)
                                callback.onFailure(error);
                        }
                    });
        });

        builder.setNegativeButton("No",
                (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
