package com.example.taxiapp.ui.estimate_route;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface OsmService {
    @GET("search")
    Call<List<OsmPlace>> search(
            @Query("q") String query,
            @Query("format") String format,
            @Query("limit") int limit
    );

}
