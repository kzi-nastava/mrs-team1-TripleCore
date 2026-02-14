package com.example.taxiapp.ui.panic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;

import java.util.List;

import model.Panic;

public class PanicAdapter extends RecyclerView.Adapter<PanicAdapter.PanicViewHolder> {

    private List<Panic> panics;
    private OnItemClickListener itemClickListener;
    private OnResolveClickListener resolveClickListener;

    public interface OnItemClickListener {
        void onItemClick(Panic panic);
    }

    public interface OnResolveClickListener {
        void onResolveClick(Long panicId);
    }

    public PanicAdapter(List<Panic> panics,
                        OnItemClickListener itemClickListener,
                        OnResolveClickListener resolveClickListener) {
        this.panics = panics;
        this.itemClickListener = itemClickListener;
        this.resolveClickListener = resolveClickListener;
    }

    @NonNull
    @Override
    public PanicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_panic, parent, false);
        return new PanicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PanicViewHolder holder, int position) {
        Panic panic = panics.get(position);

        holder.tvDriverName.setText("Driver: " + panic.getDriverName());
        holder.tvPassengerName.setText("Passenger: " + panic.getPassengerName());
        holder.tvLocation.setText("Location: " + panic.getLocation());
        holder.tvVehicle.setText("Vehicle: " + panic.getVehicle() + " - " + panic.getLicensePlate());
        holder.tvTime.setText("Time: " + panic.getTime());

        if (panic.isResolved()) {
            holder.tvStatus.setText("RESOLVED");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark));
            holder.btnResolve.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setText("ACTIVE");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_red_dark));
            holder.btnResolve.setVisibility(View.VISIBLE);
        }

        holder.btnResolve.setOnClickListener(v -> {
            if (resolveClickListener != null) {
                resolveClickListener.onResolveClick(panic.getId());
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(panic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return panics.size();
    }

    public void updateData(List<Panic> newPanics) {
        this.panics = newPanics;
        notifyDataSetChanged();
    }

    static class PanicViewHolder extends RecyclerView.ViewHolder {
        TextView tvDriverName, tvPassengerName, tvLocation, tvVehicle, tvTime, tvStatus;
        Button btnResolve;

        PanicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDriverName = itemView.findViewById(R.id.tvDriverName);
            tvPassengerName = itemView.findViewById(R.id.tvPassengerName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvVehicle = itemView.findViewById(R.id.tvVehicle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnResolve = itemView.findViewById(R.id.btnResolve);
        }
    }
}