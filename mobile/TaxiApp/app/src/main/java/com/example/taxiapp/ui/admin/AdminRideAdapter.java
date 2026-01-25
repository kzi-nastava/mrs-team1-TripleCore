package com.example.taxiapp.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;
import model.RideDetailsDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminRideAdapter extends RecyclerView.Adapter<AdminRideAdapter.RideViewHolder> {

    private List<RideDetailsDTO> rides = new ArrayList<>();
    private final Context context;
    private final OnRideClickListener listener;

    public interface OnRideClickListener {
        void onRideClick(RideDetailsDTO ride);
    }

    public AdminRideAdapter(Context context, OnRideClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void submitList(List<RideDetailsDTO> rideList) {
        this.rides = rideList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_admin_ride_card, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        RideDetailsDTO ride = rides.get(position);

        String route = ride.startLocation + " → " + ride.endLocation;
        holder.tvRoute.setText(route);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        String time = sdf.format(ride.startTime) + " - " + sdf.format(ride.endTime);
        holder.tvDateTime.setText(time);

        holder.tvPrice.setText(ride.price + " RSD");

        // Status
        if (ride.status != null) {
            switch (ride.status) {
                case "REQUESTED":
                    holder.tvStatus.setText("Requested");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.blue));
                    break;
                case "ACCEPTED":
                    holder.tvStatus.setText("Accepted");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.teal));
                    break;
                case "REJECTED":
                    holder.tvStatus.setText("Rejected");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                    break;
                case "IN_PROGRESS":
                    holder.tvStatus.setText("In Progress");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.orange));
                    break;
                case "CANCELLED":
                    holder.tvStatus.setText("Cancelled");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                    break;
                case "FINISHED":
                    holder.tvStatus.setText("Finished");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
                    break;
                default:
                    holder.tvStatus.setText("Unknown");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        }

        // Panic
        if (ride.panic) {
            holder.tvPanic.setVisibility(View.VISIBLE);
        } else {
            holder.tvPanic.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onRideClick(ride));
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    static class RideViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoute, tvDateTime, tvPrice, tvStatus, tvPanic;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoute = itemView.findViewById(R.id.tvAdminRoute);
            tvDateTime = itemView.findViewById(R.id.tvAdminDateTime);
            tvPrice = itemView.findViewById(R.id.tvAdminPrice);
            tvStatus = itemView.findViewById(R.id.tvAdminStatus);
            tvPanic = itemView.findViewById(R.id.tvAdminPanic);
        }
    }
}
