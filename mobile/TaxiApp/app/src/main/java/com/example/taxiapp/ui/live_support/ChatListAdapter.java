package com.example.taxiapp.ui.live_support;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;

import java.util.List;
import model.ChatResponse;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    public interface OnChatClickListener {
        void onChatClick(ChatResponse chat);
    }

    private List<ChatResponse> chats;
    private OnChatClickListener listener;

    public ChatListAdapter(List<ChatResponse> chats, OnChatClickListener listener) {
        this.chats = chats;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatResponse chat = chats.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView chatUserName, lastMessage;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatUserName = itemView.findViewById(R.id.chatUserName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }

        void bind(ChatResponse chat) {
            chatUserName.setText(chat.userName);
            if (chat.messages != null && !chat.messages.isEmpty()) {
                lastMessage.setText(chat.messages.get(chat.messages.size() - 1).text);
            } else {
                lastMessage.setText("");
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onChatClick(chat);
            });
        }
    }
}
