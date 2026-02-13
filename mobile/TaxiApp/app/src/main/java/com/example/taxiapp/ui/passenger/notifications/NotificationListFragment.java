package com.example.taxiapp.ui.passenger.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;

import java.util.ArrayList;
import java.util.List;

import model.NotificationResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.AuthService;
import service.NotificationService;

public class NotificationListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvNoNotifications;
    private NotificationAdapter adapter;
    private List<NotificationResponse> notifications = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerNotifications);
        tvNoNotifications = view.findViewById(R.id.tvNoNotifications);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(notifications, this::openPopup);
        recyclerView.setAdapter(adapter);

        loadNotifications();

        return view;
    }

    private void loadNotifications() {
        Long passengerId = AuthService.getInstance().getLoggedInUserId(requireContext());

        NotificationService.getInstance()
                .getPassengerNotifications(passengerId, new Callback<List<NotificationResponse>>() {

                    @Override
                    public void onResponse(Call<List<NotificationResponse>> call,
                                           Response<List<NotificationResponse>> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            notifications.clear();
                            notifications.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            updateVisibility();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                        Toast.makeText(getContext(),
                                "Failed to load notifications",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateVisibility() {
        if (notifications.isEmpty()) {
            tvNoNotifications.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoNotifications.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void openPopup(NotificationResponse notification) {
        NotificationFragment fragment =
                NotificationFragment.newInstance(notification);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();

        markAsSeen(notification);
    }

    private void markAsSeen(NotificationResponse notification) {

        if (notification.seen) return;

        NotificationService.getInstance()
                .markNotificationSeen(notification.id,
                        new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    notification.seen = true;

                                    int position = notifications.indexOf(notification);
                                    if (position != -1) {
                                        adapter.notifyItemChanged(position);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(),
                                        "Failed to mark notification as seen",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
    }
}
