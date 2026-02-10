package helper;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import com.example.taxiapp.R;

public class StatusColorHelper {

    public static int getStatusColor(Context context, String status) {
        if (status == null) {
            return ContextCompat.getColor(context, R.color.black);
        }

        @ColorRes int colorRes = getStatusColorResource(status);
        return ContextCompat.getColor(context, colorRes);
    }

    public static @ColorRes int getStatusColorResource(String status) {
        if (status == null) {
            return R.color.black;
        }

        switch (status.toUpperCase()) {
            case "REQUESTED":
                return R.color.blue;
            case "ACCEPTED":
                return R.color.teal;
            case "REJECTED":
                return R.color.red;
            case "IN_PROGRESS":
                return R.color.orange;
            case "CANCELLED":
                return R.color.red;
            case "FINISHED":
                return R.color.green;
            default:
                return R.color.black;
        }
    }

    public static String getStatusDisplayText(String status) {
        if (status == null) return "Unknown";

        switch (status.toUpperCase()) {
            case "REQUESTED":
                return "Requested";
            case "ACCEPTED":
                return "Accepted";
            case "REJECTED":
                return "Rejected";
            case "IN_PROGRESS":
                return "In Progress";
            case "CANCELLED":
                return "Cancelled";
            case "FINISHED":
                return "Finished";
            default:
                return status;
        }
    }
}