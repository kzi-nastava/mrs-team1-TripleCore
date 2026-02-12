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

public class CancelRideHelper {

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
                new retrofit2.Callback<ResponseBody>() {

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
}
