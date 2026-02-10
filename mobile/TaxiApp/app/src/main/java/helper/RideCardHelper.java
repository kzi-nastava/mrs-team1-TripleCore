package helper;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taxiapp.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;

import model.RideDetailsDTO;

public class RideCardHelper {

    public interface OnRideClickListener {
        void onRideClick(RideDetailsDTO ride);
    }

    public static void loadRideCards(LinearLayout container, List<RideDetailsDTO> rides,
                                     LayoutInflater inflater, OnRideClickListener listener) {
        if (container == null) return;

        container.removeAllViews();

        if (rides == null || rides.isEmpty()) {
            return;
        }

        for (RideDetailsDTO ride : rides) {
            MaterialCardView card = (MaterialCardView)
                    inflater.inflate(R.layout.view_passenger_ride_card, container, false);

            bindRideData(card, ride);
            card.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRideClick(ride);
                }
            });

            container.addView(card);
        }
    }

    public static void bindRideData(MaterialCardView card, RideDetailsDTO ride) {
        if (card == null || ride == null) return;

        TextView tvRoute = card.findViewById(R.id.tvPassengerRoute);
        TextView tvStart = card.findViewById(R.id.tvPassengerStartAddress);
        TextView tvEnd = card.findViewById(R.id.tvPassengerEndAddress);
        TextView tvDateTime = card.findViewById(R.id.tvPassengerDateTime);
        TextView tvPrice = card.findViewById(R.id.tvPassengerPrice);
        TextView tvStatus = card.findViewById(R.id.tvPassengerStatus);
        TextView tvPanic = card.findViewById(R.id.tvPassengerPanic);

        // Route
        if (ride.startLocation != null && ride.endLocation != null) {
            String route = ride.startLocation.address + " → " + ride.endLocation.address;
            tvRoute.setText(route);
            tvStart.setText(ride.startLocation.address);
            tvEnd.setText(ride.endLocation.address);
        } else {
            tvRoute.setText("Route not available");
            tvStart.setText("N/A");
            tvEnd.setText("N/A");
        }

        // Date and Time
        String start = ride.startTime != null ? ride.startTime : "N/A";
        String end = ride.endTime != null ? ride.endTime : "N/A";
        tvDateTime.setText(start + " - " + end);

        // Price
        try {
            double priceValue = Double.parseDouble(String.valueOf(ride.price));
            String formattedPrice = String.format(Locale.getDefault(), "%.2f RSD", priceValue);
            tvPrice.setText(formattedPrice);
        } catch (NumberFormatException | NullPointerException e) {
            tvPrice.setText(ride.price + " RSD");
        }

        // Status
        if (ride.status != null) {
            tvStatus.setText(StatusColorHelper.getStatusDisplayText(ride.status));
            tvStatus.setTextColor(
                    StatusColorHelper.getStatusColor(card.getContext(), ride.status)
            );
        } else {
            tvStatus.setText("Unknown");
            tvStatus.setTextColor(StatusColorHelper.getStatusColor(card.getContext(), null));
        }

        // Panic indicator
        tvPanic.setVisibility(ride.panic ? View.VISIBLE : View.GONE);
    }
}