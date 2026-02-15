package helper;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import model.RideDetailsDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.RideService;

public class PanicHelper {

    public interface PanicCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    public static void showPanicDialog(Context context,
                                       RideDetailsDTO ride,
                                       Long userId,
                                       PanicCallback callback) {

        if (ride == null || userId == null) {
            if (callback != null) callback.onFailure("Invalid ride or user");
            return;
        }

        new AlertDialog.Builder(context)
                .setTitle("PANIC")
                .setMessage("Do you want to activate panic mode? This will alert administrators.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();

                    RideService.getInstance().activatePanic(ride.id, userId,
                            new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "PANIC activated", Toast.LENGTH_SHORT).show();
                                        if (callback != null) callback.onSuccess("Panic activated");
                                    } else {
                                        String error = "Failed to activate panic: " + response.code();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                        if (callback != null) callback.onFailure(error);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    String error = "Network error: " + t.getMessage();
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    if (callback != null) callback.onFailure(error);
                                }
                            });
                })
                .setNegativeButton("No", null)
                .show();
    }
}