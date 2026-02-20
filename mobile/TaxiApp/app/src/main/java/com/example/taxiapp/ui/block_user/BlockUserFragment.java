package com.example.taxiapp.ui.block_user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;
import com.example.taxiapp.ui.user_block_adapter.UserBlockAdapter;

import java.util.ArrayList;
import java.util.List;

import model.UserBlockedResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.AdminService;

public class BlockUserFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserBlockAdapter adapter;
    private List<UserBlockedResponse> userList = new ArrayList<>();

    public BlockUserFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_block_user, container, false);


        recyclerView = v.findViewById(R.id.rvUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        loadUsers();

        return v;
    }

    private void loadUsers() {

        AdminService.getInstance().getNonAdminUsers(new Callback<List<UserBlockedResponse>>() {
            @Override
            public void onResponse(Call<List<UserBlockedResponse>> call, Response<List<UserBlockedResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userList = response.body();


                    long currentAdminId = getActivity()
                            .getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                            .getLong("userId", -1);

                    userList.removeIf(u -> u.getId() == currentAdminId);


                    adapter = new UserBlockAdapter(userList, new UserBlockAdapter.OnBlockClickListener() {
                        @Override
                        public void onBlockClick(UserBlockedResponse user, String reason) {

                            executeBlockUser(user, reason);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Server error: Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserBlockedResponse>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void executeBlockUser(UserBlockedResponse user, String reason) {

        AdminService.getInstance().blockUser(user.getId(), reason, new Callback<UserBlockedResponse>() {
            @Override
            public void onResponse(Call<UserBlockedResponse> call, Response<UserBlockedResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    user.setBlocked(true);


                    adapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), "User " + user.getFirstname() + " has been blocked.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Failed to block user. Try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserBlockedResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error connecting to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}