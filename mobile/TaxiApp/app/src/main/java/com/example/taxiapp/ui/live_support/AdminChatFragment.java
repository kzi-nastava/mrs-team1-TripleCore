package com.example.taxiapp.ui.live_support;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.live_support.ChatListFragment;

import java.io.Serializable;
import java.util.List;

import model.ChatResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.ChatService;

public class AdminChatFragment extends Fragment {

    public AdminChatFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            loadChats();
        }
    }

    private void loadChats() {
        ChatService.getInstance()
                .getAllChats()
                .enqueue(new Callback<List<ChatResponse>>() {

                    @Override
                    public void onResponse(Call<List<ChatResponse>> call,
                                           Response<List<ChatResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            openChatListFragment(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ChatResponse>> call,
                                          Throwable t) {
                        Toast.makeText(
                                requireContext(),
                                "Failed fetching chats",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void openChatListFragment(List<ChatResponse> chats) {
        FragmentManager fm = requireActivity().getSupportFragmentManager();

        // remove this fragment from backstack to prevent
        // loading from backend every time you go back from the chat list
        if (fm.getBackStackEntryCount() != 0) {
            Fragment topFragment = fm.getFragments().get(fm.getFragments().size() - 1);
            if (topFragment instanceof AdminChatFragment) {
                fm.popBackStack();
            }
        }

        ChatListFragment fragment = ChatListFragment.newInstance(chats);
        fm.beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }


}
