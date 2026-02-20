package com.example.taxiapp.ui.favorite_routes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taxiapp.R;
import com.example.taxiapp.ui.favorite_routes_adapter.FavoriteRoutesAdapter;
import com.example.taxiapp.ui.order_ride.OrderRideFragment;
import java.util.List;
import model.FavoriteRouteResponse;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteRoutesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteRoutesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_routes, container, false);
        recyclerView = view.findViewById(R.id.rvFavoriteRoutes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchFavorites();
        return view;
    }

    private void fetchFavorites() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        Long passengerId = sharedPreferences.getLong("userId", -1);

        RetrofitClient.getApiService().getFavoriteRoutes(passengerId).enqueue(new Callback<List<FavoriteRouteResponse>>() {
            @Override
            public void onResponse(Call<List<FavoriteRouteResponse>> call, Response<List<FavoriteRouteResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FavoriteRouteResponse> routes = response.body();
                    adapter = new FavoriteRoutesAdapter(routes, new FavoriteRoutesAdapter.OnFavoriteActionListener() {
                        @Override
                        public void onSelect(FavoriteRouteResponse route) {
                            sendToOrderForm(route);
                        }

                        @Override
                        public void onRemove(FavoriteRouteResponse route) {
                            deleteRoute(passengerId, route.getId());
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<FavoriteRouteResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteRoute(Long passengerId, Long routeId) {
        RetrofitClient.getApiService().removeFavoriteRoute(passengerId, routeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    fetchFavorites(); // Osveži listu
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    private void sendToOrderForm(FavoriteRouteResponse route) {
        Bundle bundle = new Bundle();
        bundle.putString("startAddr", route.getStartAddress());
        bundle.putDouble("startLat", route.getStartLat());
        bundle.putDouble("startLon", route.getStartLon());
        bundle.putString("endAddr", route.getEndAddress());
        bundle.putDouble("endLat", route.getEndLat());
        bundle.putDouble("endLon", route.getEndLon());

        OrderRideFragment fragment = new OrderRideFragment();
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null).commit();
    }
}