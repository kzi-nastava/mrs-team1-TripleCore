package service;

import android.util.Log;

import model.Panic;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class PanicService {

    private static PanicService instance;
    private static final String TAG = "PanicService";

    private PanicService() {}

    public static PanicService getInstance() {
        if (instance == null) {
            instance = new PanicService();
        }
        return instance;
    }

    public void getAllPanics(Callback<List<Panic>> callback) {
        RetrofitClient.getApiService().getAllPanics().enqueue(new Callback<List<Panic>>() {
            @Override
            public void onResponse(Call<List<Panic>> call, Response<List<Panic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "getAllPanics success: " + response.body().size() + " panics");
                    callback.onResponse(call, response);
                } else {
                    Log.e(TAG, "getAllPanics failed: " + response.message());
                    callback.onFailure(call, new Throwable("Failed to get all panics: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Panic>> call, Throwable t) {
                Log.e(TAG, "getAllPanics error", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void getActivePanics(Callback<List<Panic>> callback) {
        RetrofitClient.getApiService().getActivePanics().enqueue(new Callback<List<Panic>>() {
            @Override
            public void onResponse(Call<List<Panic>> call, Response<List<Panic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "getActivePanics success: " + response.body().size() + " active panics");
                    callback.onResponse(call, response);
                } else {
                    Log.e(TAG, "getActivePanics failed: " + response.message());
                    callback.onFailure(call, new Throwable("Failed to get active panics: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Panic>> call, Throwable t) {
                Log.e(TAG, "getActivePanics error", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void getResolvedPanics(Callback<List<Panic>> callback) {
        RetrofitClient.getApiService().getResolvedPanics().enqueue(new Callback<List<Panic>>() {
            @Override
            public void onResponse(Call<List<Panic>> call, Response<List<Panic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "getResolvedPanics success: " + response.body().size() + " resolved panics");
                    callback.onResponse(call, response);
                } else {
                    Log.e(TAG, "getResolvedPanics failed: " + response.message());
                    callback.onFailure(call, new Throwable("Failed to get resolved panics: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Panic>> call, Throwable t) {
                Log.e(TAG, "getResolvedPanics error", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void resolvePanic(Long panicId, Callback<Void> callback) {
        RetrofitClient.getApiService().resolvePanic(panicId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "resolvePanic success for panic ID: " + panicId);
                    callback.onResponse(call, response);
                } else {
                    Log.e(TAG, "resolvePanic failed: " + response.message());
                    callback.onFailure(call, new Throwable("Failed to resolve panic: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "resolvePanic error", t);
                callback.onFailure(call, t);
            }
        });
    }

    public void hasActivePanics(Callback<Boolean> callback) {
        getActivePanics(new Callback<List<Panic>>() {
            @Override
            public void onResponse(Call<List<Panic>> call, Response<List<Panic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean hasActive = !response.body().isEmpty();
                    Log.d(TAG, "hasActivePanics: " + hasActive + " (" + response.body().size() + " active)");

                    new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            callback.onFailure(null, t);
                        }
                    }.onResponse(null, Response.success(hasActive));

                } else {
                    callback.onFailure(null, new Throwable("Failed to check active panics"));
                }
            }

            @Override
            public void onFailure(Call<List<Panic>> call, Throwable t) {
                Log.e(TAG, "hasActivePanics error", t);
                callback.onFailure(null, t);
            }
        });
    }
}