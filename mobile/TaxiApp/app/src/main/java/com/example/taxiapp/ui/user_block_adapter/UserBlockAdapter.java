package com.example.taxiapp.ui.user_block_adapter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;

import java.util.List;

import model.UserBlockedResponse;

public class UserBlockAdapter extends RecyclerView.Adapter<UserBlockAdapter.ViewHolder> {
    private List<UserBlockedResponse> users;
    private OnBlockClickListener listener;

    public interface OnBlockClickListener {
        void onBlockClick(UserBlockedResponse user, String reason);
    }

    public UserBlockAdapter(List<UserBlockedResponse> users, OnBlockClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_block, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserBlockedResponse user = users.get(position);
        holder.tvName.setText(user.getFirstname() + " " + user.getLastname());
        holder.tvEmail.setText(user.getEmail());

        if (user.isBlocked()) {
            holder.tvStatus.setText("Status: Blocked");
            holder.tvStatus.setTextColor(Color.RED);
            holder.btnBlock.setVisibility(View.GONE);
            holder.etReason.setVisibility(View.GONE);
            holder.tvAlreadyBlocked.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText("Status: Active");
            holder.tvStatus.setTextColor(Color.parseColor("#28a745"));
            holder.btnBlock.setVisibility(View.VISIBLE);
            holder.etReason.setVisibility(View.VISIBLE);
            holder.tvAlreadyBlocked.setVisibility(View.GONE);
        }

        holder.btnBlock.setOnClickListener(v -> {
            String reason = holder.etReason.getText().toString();
            listener.onBlockClick(user, reason);
        });
    }

    @Override
    public int getItemCount() { return users.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvStatus, tvAlreadyBlocked;
        EditText etReason;
        Button btnBlock;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserName);
            tvEmail = itemView.findViewById(R.id.tvUserEmail);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAlreadyBlocked = itemView.findViewById(R.id.tvAlreadyBlocked);
            etReason = itemView.findViewById(R.id.etBlockReason);
            btnBlock = itemView.findViewById(R.id.btnBlock);
        }
    }
}