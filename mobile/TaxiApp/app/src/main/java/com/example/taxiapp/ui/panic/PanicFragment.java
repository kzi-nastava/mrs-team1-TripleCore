package com.example.taxiapp.ui.panic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import model.Panic;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.PanicService;

public class PanicFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View emptyState;
    private TabLayout tabLayout;
    private androidx.appcompat.widget.Toolbar toolbar;

    private PanicAdapter adapter;
    private PanicService panicService;

    private String currentFilter = "ALL"; // ALL, ACTIVE, RESOLVED

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        panicService = PanicService.getInstance();

        setupToolbar();

        setupTabs();

        setupRecyclerView();

        loadPanics("ALL");
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        emptyState = view.findViewById(R.id.emptyState);
        tabLayout = view.findViewById(R.id.tabLayout);
        toolbar = view.findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_refresh) {
                refreshData();
                return true;
            }
            return false;
        });
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentFilter = "ALL";
                        break;
                    case 1:
                        currentFilter = "ACTIVE";
                        break;
                    case 2:
                        currentFilter = "RESOLVED";
                        break;
                }
                loadPanics(currentFilter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                loadPanics(currentFilter);
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new PanicAdapter(
                new ArrayList<>(),
                panic -> showPanicDetails(panic),
                panicId -> showResolveConfirmation(panicId)
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadPanics(String filter) {
        showLoading(true);

        switch (filter) {
            case "ACTIVE":
                panicService.getActivePanics(new Callback<List<Panic>>() {
                    @Override
                    public void onResponse(Call<List<Panic>> call, Response<List<Panic>> response) {
                        if (isAdded()) {
                            showLoading(false);
                            if (response.isSuccessful() && response.body() != null) {
                                updateUI(response.body());
                            } else {
                                showError("Failed to load active panics");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Panic>> call, Throwable t) {
                        if (isAdded()) {
                            showLoading(false);
                            showError(t.getMessage());
                        }
                    }
                });
                break;

            case "RESOLVED":
                panicService.getResolvedPanics(new Callback<List<Panic>>() {
                    @Override
                    public void onResponse(Call<List<Panic>> call, Response<List<Panic>> response) {
                        if (isAdded()) {
                            showLoading(false);
                            if (response.isSuccessful() && response.body() != null) {
                                updateUI(response.body());
                            } else {
                                showError("Failed to load resolved panics");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Panic>> call, Throwable t) {
                        if (isAdded()) {
                            showLoading(false);
                            showError(t.getMessage());
                        }
                    }
                });
                break;

            default: // ALL
                panicService.getAllPanics(new Callback<List<Panic>>() {
                    @Override
                    public void onResponse(Call<List<Panic>> call, Response<List<Panic>> response) {
                        if (isAdded()) {
                            showLoading(false);
                            if (response.isSuccessful() && response.body() != null) {
                                updateUI(response.body());
                            } else {
                                showError("Failed to load all panics");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Panic>> call, Throwable t) {
                        if (isAdded()) {
                            showLoading(false);
                            showError(t.getMessage());
                        }
                    }
                });
                break;
        }
    }

    private void updateUI(List<Panic> panics) {
        if (panics.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            adapter.updateData(panics);
        }
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showError(String message) {
        Snackbar.make(requireView(), "Error: " + message, Snackbar.LENGTH_LONG).show();
    }

    private void showPanicDetails(Panic panic) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Panic Details")
                .setMessage(
                        "Driver: " + panic.getDriverName() + "\n" +
                                "Passenger: " + panic.getPassengerName() + "\n" +
                                "Location: " + panic.getLocation() + "\n" +
                                "Vehicle: " + panic.getVehicle() + "\n" +
                                "License Plate: " + panic.getLicensePlate() + "\n" +
                                "Time: " + panic.getTime() + "\n" +
                                "Status: " + (panic.isResolved() ? "RESOLVED" : "ACTIVE")
                )
                .setPositiveButton("OK", null)
                .show();
    }

    private void showResolveConfirmation(Long panicId) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Resolve Panic")
                .setMessage("Are you sure you want to mark this panic as resolved?")
                .setPositiveButton("Yes", (dialog, which) -> resolvePanic(panicId))
                .setNegativeButton("No", null)
                .show();
    }

    private void resolvePanic(Long panicId) {
        showLoading(true);

        panicService.resolvePanic(panicId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (isAdded()) {
                    showLoading(false);
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Panic resolved successfully", Toast.LENGTH_SHORT).show();
                        loadPanics(currentFilter);
                    } else {
                        showError("Failed to resolve panic");
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (isAdded()) {
                    showLoading(false);
                    showError(t.getMessage());
                }
            }
        });
    }

    private void refreshData() {
        loadPanics(currentFilter);
    }
}