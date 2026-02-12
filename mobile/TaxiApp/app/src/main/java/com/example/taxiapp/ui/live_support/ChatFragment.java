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
    private TextView userNameText;

    private ChatAdapter adapter;
    private ChatResponse chatResponse;

    private Long currentUserId; // postavi iz Session / SharedPrefs

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        currentUserId = AuthService.getInstance().getLoggedInUserId(requireContext());

        recyclerView = view.findViewById(R.id.messagesRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);
        userNameText = view.findViewById(R.id.chatUserName);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // TODO: učitaj ChatResponse sa backenda
        loadChat();

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

    private void loadChat() {

        chatResponse = new ChatResponse();
        chatResponse.chatId = 100L;
        chatResponse.userId = 2L;
        chatResponse.userName = "Marko Petrović";

        List<MessageResponse> mockMessages = new ArrayList<>();

        MessageResponse m1 = new MessageResponse();
        m1.text = "Zdravo!";
        m1.senderId = 2L;
        m1.sentAt = LocalDateTime.now().minusMinutes(15);
        mockMessages.add(m1);

        MessageResponse m2 = new MessageResponse();
        m2.text = "Zdravo, kako si?";
        m2.senderId = 1L;
        m2.sentAt = LocalDateTime.now().minusMinutes(14);
        mockMessages.add(m2);

        MessageResponse m3 = new MessageResponse();
        m3.text = "Treba mi vožnja za sutra ujutru.";
        m3.senderId = 2L;
        m3.sentAt = LocalDateTime.now().minusMinutes(10);
        mockMessages.add(m3);

        MessageResponse m4 = new MessageResponse();
        m4.text = "Naravno, u koje vreme?";
        m4.senderId = 1L;
        m4.sentAt = LocalDateTime.now().minusMinutes(9);
        mockMessages.add(m4);

        MessageResponse m5 = new MessageResponse();
        m5.text = "U 08:00 sa Novog Beograda.";
        m5.senderId = 2L;
        m5.sentAt = LocalDateTime.now().minusMinutes(5);
        mockMessages.add(m5);

        chatResponse.messages = mockMessages;

        // Povezivanje adaptera
        adapter = new ChatAdapter(chatResponse.messages, currentUserId);
        recyclerView.setAdapter(adapter);

        userNameText.setText(chatResponse.userName);

        recyclerView.scrollToPosition(chatResponse.messages.size() - 1);
    }


    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        MessageResponse message = new MessageResponse();
        message.text = text;
        message.senderId = currentUserId;
        message.sentAt = LocalDateTime.now();

        chatResponse.messages.add(message);
        adapter.notifyItemInserted(chatResponse.messages.size() - 1);
        recyclerView.scrollToPosition(chatResponse.messages.size() - 1);

        messageInput.setText("");

        // TODO: pozvati backend da se poruka sačuva
    }
}
