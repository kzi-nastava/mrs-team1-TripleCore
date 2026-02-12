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
import service.AuthService;
import service.ChatService;
import model.ChatResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserChatFragment extends Fragment {

    public UserChatFragment() {
        // Required empty public constructor
    }

    public static UserChatFragment newInstance() {
        return new UserChatFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Long userId = AuthService.getInstance().getLoggedInUserId(requireContext());
        if (savedInstanceState == null){
            fetchUserChat(userId);
        }

    }

    private void fetchUserChat(Long userId) {
        ChatService.getInstance()
                .getUserChat(userId)
                .enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            openChatFragment(response.body());
                        } else {
                            Toast.makeText(requireContext(), "Failed to fetch chat", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {
                        Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openChatFragment(ChatResponse chatResponse) {
        FragmentManager fm = requireActivity().getSupportFragmentManager();

        // remove from backstack to prevent reloading the chat on back
        if (fm.getBackStackEntryCount() != 0) {
            Fragment topFragment = fm.getFragments().get(fm.getFragments().size() - 1);
            if (topFragment instanceof UserChatFragment) {
                fm.popBackStack();
            }
        }

        ChatFragment fragment = ChatFragment.newInstance(chatResponse);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
