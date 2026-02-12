package com.example.taxiapp.ui.live_support;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taxiapp.R;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.ChatResponse;
import model.MessageResponse;
import service.AuthService;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private TextView chatHeader;

    private ChatAdapter adapter;
    private ChatResponse chatResponse;

    private Long currentUserId;
    private String currentUserRole;

    private static final String ARG_CHAT = "arg_chat";

    public static ChatFragment newInstance(ChatResponse chatResponse) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHAT, chatResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            chatResponse = (ChatResponse) getArguments().getSerializable(ARG_CHAT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        currentUserId = AuthService.getInstance().getLoggedInUserId(requireContext());
        currentUserRole = AuthService.getInstance().getLoggedInUserRole(requireContext());

        recyclerView = view.findViewById(R.id.messagesRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);
        chatHeader = view.findViewById(R.id.chatHeader);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (chatResponse != null) {
            adapter = new ChatAdapter(chatResponse.messages, currentUserId);
            recyclerView.setAdapter(adapter);

            if ("ADMIN".equals(currentUserRole))
                chatHeader.setText(chatResponse.userName);
            else
                chatHeader.setText("Live Support");

            recyclerView.scrollToPosition(chatResponse.messages.size() - 1);
        }

        messageInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendButton.setEnabled(!s.toString().trim().isEmpty());
            }
        });

        sendButton.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        MessageResponse message = new MessageResponse();
        message.text = text;
        message.senderId = currentUserId;
        message.sentAt = LocalDateTime.now().toString();

        chatResponse.messages.add(message);
        adapter.notifyItemInserted(chatResponse.messages.size() - 1);
        recyclerView.scrollToPosition(chatResponse.messages.size() - 1);

        messageInput.setText("");

        // TODO: pozvati backend da se poruka sačuva
    }
}
