package com.example.taxiapp.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxiapp.R;

import java.util.ArrayList;
import java.util.List;

import model.DriverProfileChangeRequestResponse;
import service.ProfileService;
import retrofit2.Call;
import retrofit2.Response;

public class DriversRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DriverRequestsAdapter adapter;
    private ProfileService profileService;
    private List<DriverProfileChangeRequestResponse> requests = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drivers_requests, container, false);

        profileService = ProfileService.getInstance();

        recyclerView = view.findViewById(R.id.rvDriverRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DriverRequestsAdapter(requests, new DriverRequestsAdapter.OnRequestActionListener() {
            @Override
            public void onApprove(DriverProfileChangeRequestResponse request) {
                changeRequestStatus(request, "APPROVED");
            }

            @Override
            public void onReject(DriverProfileChangeRequestResponse request) {
                changeRequestStatus(request, "REJECTED");
            }
        });

        recyclerView.setAdapter(adapter);

        loadDriverRequests();

        return view;
    }

    private void loadDriverRequests() {
        profileService.getAllDriverProfileRequests(new retrofit2.Callback<List<DriverProfileChangeRequestResponse>>() {
            @Override
            public void onResponse(Call<List<DriverProfileChangeRequestResponse>> call,
                                   Response<List<DriverProfileChangeRequestResponse>> response) {
                Log.d("DriverRequests", "onResponse called");
                if (response.isSuccessful()) {
                    List<DriverProfileChangeRequestResponse> list = response.body();
                    Log.d("DriverRequests", "Response size: " + (list != null ? list.size() : "null"));
                    if (list != null) {
                        for (DriverProfileChangeRequestResponse r : list) {
                            Log.d("DriverRequests", "Item: email=" + r.getEmail() + " status=" + r.getStatus());
                        }
                        requests = list;
                        adapter.updateList(requests);
                    }
                } else {
                    Log.e("DriverRequests", "Response failed! Code: " + response.code());
                    try {
                        if (response.errorBody() != null)
                            Log.e("DriverRequests", "Error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "Failed to load requests", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<DriverProfileChangeRequestResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeRequestStatus(DriverProfileChangeRequestResponse request, String status) {
        if ("APPROVED".equals(status)) {
            profileService.approveDriverRequest(request.getId(), new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        request.setStatus("APPROVED");
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Request approved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to approve request", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Request approved", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if ("REJECTED".equals(status)) {
            profileService.rejectDriverRequest(request.getId(), new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        request.setStatus("REJECTED");
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Request rejected", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to reject request", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(), "Request rejected successfuly", Toast.LENGTH_SHORT).show();

//                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
