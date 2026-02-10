package helper;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import model.RideDetailsDTO;
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
            // Driver must provide a reason for cancellation
            final EditText input = new EditText(context);
            input.setHint("Enter cancellation reason (required)");

            builder.setView(input);
            builder.setMessage("Driver must provide a cancellation reason:");

            builder.setPositiveButton("Cancel Ride", (dialog, which) -> {
                String reason = input.getText().toString().trim();
                if (reason.isEmpty()) {
                    Toast.makeText(context, "Reason is required for driver", Toast.LENGTH_SHORT).show();
                    showCancelDialog(context, ride, userRole, userId, callback); // Re-open
                } else {
                    executeCancelRide(ride.id, userRole, reason, userId, callback);
                }
            });

        } else if ("PASSENGER".equals(userRole)) {
            // Passenger can cancel without reason but
            builder.setMessage("Are you sure you want to cancel this ride?");
            builder.setPositiveButton("Yes, Cancel", (dialog, which) -> {
                executeCancelRide(ride.id, userRole, "", userId, callback);
            });
        } else {
            // Should not happen due to canCancelRide check, but just in case
            Toast.makeText(context, "You don't have permission to cancel this ride", Toast.LENGTH_SHORT).show();
            if (callback != null) callback.onFailure("Permission denied");
            return;
        }

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private static String getCannotCancelMessage(RideDetailsDTO ride, String userRole) {
        if (ride == null) return "Invalid ride";

        String status = ride.status;

        if ("FINISHED".equals(status)) {
            return "Cannot cancel a finished ride";
        }

        if ("CANCELLED".equals(status)) {
            return "Ride is already cancelled";
        }

        if ("IN_PROGRESS".equals(status)) {
            return "Cannot cancel a ride in progress";
        }

        if (!"PASSENGER".equals(userRole) && !"DRIVER".equals(userRole)) {
            return "You don't have permission to cancel rides";
        }

        return "Cannot cancel this ride";
    }

    private static void executeCancelRide(Long rideId, String userRole, String reason,
                                          Long userId, CancelRideCallback callback) {

        String cancelerType = "PASSENGER".equals(userRole) ? "PASSENGER" : "DRIVER";

        RideService.getInstance().cancelRide(rideId, cancelerType, reason,
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (callback != null) callback.onSuccess();
                        } else {
                            String error = "Failed to cancel ride";

                            try {
                                if (response.errorBody() != null) {
                                    String errorBody = response.errorBody().string();

                                    if (errorBody.contains("Passenger can only cancel 10 minutes before ride start")) {
                                        error = "You can only cancel a ride up to 10 minutes before it starts";
                                    } else if (errorBody.contains("Driver must provide a cancellation reason")) {
                                        error = "Driver must provide a cancellation reason";
                                    } else if (errorBody.contains("Ride with ID") && errorBody.contains("not found")) {
                                        error = "Ride not found";
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (response.code() == 400) {
                                    error = "Bad request - cannot cancel this ride";
                                } else if (response.code() == 404) {
                                    error = "Ride not found";
                                } else if (response.code() == 500) {
                                    error = "Server error";
                                }
                            }

                            if (callback != null) callback.onFailure(error);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        String error = "Network error: " + t.getMessage();
                        if (callback != null) callback.onFailure(error);
                    }
                });
    }

}