package com.example.taxiapp.ui.live_support;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

import model.MessageResponse;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_OWN = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<MessageResponse> messages;
    private Long currentUserId;

    public ChatAdapter(List<MessageResponse> messages, Long currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).senderId.equals(currentUserId)
                ? VIEW_TYPE_OWN
                : VIEW_TYPE_OTHER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_OWN) {
            return new OwnViewHolder(inflater.inflate(R.layout.own_message, parent, false));
        } else {
            return new OtherViewHolder(inflater.inflate(R.layout.other_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageResponse message = messages.get(position);

        if (holder instanceof OwnViewHolder) {
            ((OwnViewHolder) holder).bind(message);
        } else {
            ((OtherViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class OwnViewHolder extends RecyclerView.ViewHolder {
        TextView text, time;

        OwnViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.messageText);
            time = itemView.findViewById(R.id.messageTime);
        }

        void bind(MessageResponse message) {
            text.setText(message.text);
            time.setText(message.sentAt);
        }
    }

    static class OtherViewHolder extends RecyclerView.ViewHolder {
        TextView text, time;

        OtherViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.messageText);
            time = itemView.findViewById(R.id.messageTime);
        }

        void bind(MessageResponse message) {
            text.setText(message.text);
            time.setText(message.sentAt);
        }
    }
}

