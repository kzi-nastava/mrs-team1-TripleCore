package com.example.taxiapp.ui.passenger;

import static helper.DateTimeHelper.getDateTime;
import static helper.DateTimeHelper.getTimeOnly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;

import java.util.List;

import model.NotificationResponse;

public class NotificationAdapter
        extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final List<NotificationResponse> notifications;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(NotificationResponse notification);
    }

    public NotificationAdapter(List<NotificationResponse> notifications,
                               OnItemClickListener listener) {
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationResponse notification = notifications.get(position);

        holder.tvTitle.setText(notification.title);
        holder.tvMessage.setText(notification.message);
        holder.tvTime.setText(getDateTime(notification.time));

        if (notification.seen) {
            holder.imgIcon.setImageResource(R.drawable.notification_check);
        } else {
            holder.imgIcon.setImageResource(R.drawable.notification_bell);
        }

        holder.cardNotification.setOnClickListener(v ->
                listener.onClick(notification));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvMessage, tvTime;
        ImageView imgIcon;
        View cardNotification;


        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            cardNotification = itemView.findViewById(R.id.cardNotification);
        }
    }
}
