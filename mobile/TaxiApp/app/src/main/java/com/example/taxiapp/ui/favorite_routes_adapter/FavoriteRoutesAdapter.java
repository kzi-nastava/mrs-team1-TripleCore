package com.example.taxiapp.ui.favorite_routes_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.taxiapp.R;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import model.FavoriteRouteResponse;

public class FavoriteRoutesAdapter extends RecyclerView.Adapter<FavoriteRoutesAdapter.ViewHolder> {

    private List<FavoriteRouteResponse> routes;
    private OnFavoriteActionListener listener;

    public interface OnFavoriteActionListener {
        void onSelect(FavoriteRouteResponse route);
        void onRemove(FavoriteRouteResponse route);
    }

    public FavoriteRoutesAdapter(List<FavoriteRouteResponse> routes, OnFavoriteActionListener listener) {
        this.routes = routes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_route, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteRouteResponse route = routes.get(position);

        holder.tvHeader.setText(route.getStartAddress() + " -> " + route.getEndAddress());
        holder.tvStart.setText(route.getStartAddress());
        holder.tvEnd.setText(route.getEndAddress());

        holder.btnSelect.setOnClickListener(v -> listener.onSelect(route));
        holder.btnRemove.setOnClickListener(v -> listener.onRemove(route));
    }

    @Override
    public int getItemCount() { return routes.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader, tvStart, tvEnd;
        MaterialButton btnSelect, btnRemove;

        public ViewHolder(View v) {
            super(v);
            tvHeader = v.findViewById(R.id.tvRouteHeader);
            tvStart = v.findViewById(R.id.tvStartAddress);
            tvEnd = v.findViewById(R.id.tvEndAddress);
            btnSelect = v.findViewById(R.id.btnSelectRoute);
            btnRemove = v.findViewById(R.id.btnRemoveFavorite);
        }
    }
}