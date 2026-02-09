package com.example.taxiapp.ui.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;

import java.util.List;

import model.DriverProfileChangeRequestResponse;

public class DriverRequestsAdapter extends RecyclerView.Adapter<DriverRequestsAdapter.ViewHolder> {

    private List<DriverProfileChangeRequestResponse> requests;
    private final OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onApprove(DriverProfileChangeRequestResponse request);
        void onReject(DriverProfileChangeRequestResponse request);
    }

    public DriverRequestsAdapter(List<DriverProfileChangeRequestResponse> requests, OnRequestActionListener listener) {
        this.requests = requests;
        this.listener = listener;
    }

    public void updateList(List<DriverProfileChangeRequestResponse> newList) {
        this.requests = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driver_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DriverProfileChangeRequestResponse request = requests.get(position);

        // Prikazujemo samo ono što DTO ima
        holder.tvEmail.setText("Email: " + request.getEmail());
        holder.tvStatus.setText("Status: " + request.getStatus());
        holder.tvCreatedAt.setText("Created: " + request.getCreatedAt());

        // Dugmad za akciju
        holder.btnApprove.setOnClickListener(v -> listener.onApprove(request));
        holder.btnReject.setOnClickListener(v -> listener.onReject(request));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmail, tvStatus, tvCreatedAt;
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tvDriverEmail);
            tvStatus = itemView.findViewById(R.id.tvRequestStatus);


            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);

            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
