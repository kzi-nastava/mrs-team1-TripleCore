package helper;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import model.RideDetailsDTO;
import model.StopRideRequest;
import model.StopRideResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.RideService;

public class StopRideHelper {

    public interface StopRideCallback {
        void onSuccess(StopRideResponse response);
        void onFailure(String error);
    }

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
