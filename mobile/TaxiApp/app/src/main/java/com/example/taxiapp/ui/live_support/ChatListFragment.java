package com.example.taxiapp.ui.live_support;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taxiapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import model.ChatResponse;

public class ChatListFragment extends Fragment {

    private static final String ARG_CHAT_LIST = "arg_chat_list";

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private List<ChatResponse> chatList = new ArrayList<>();

    public static ChatListFragment newInstance(List<ChatResponse> chats) {
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CHAT_LIST, (Serializable) chats);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatList = (List<ChatResponse>) getArguments().getSerializable(ARG_CHAT_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        recyclerView = view.findViewById(R.id.chatListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ChatListAdapter(chatList, chat -> {
            // klik na chat → otvori ChatFragment
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, ChatFragment.newInstance(chat))
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);

        return view;
    }
}
