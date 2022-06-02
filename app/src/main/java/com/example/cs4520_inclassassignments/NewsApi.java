package com.example.cs4520_inclassassignments;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NewsApi {
    String API_KEY = "f777477991854b12a9a2f60117e85e34";

    @Headers("X-Api-Key:" + API_KEY)
    @GET("/v2/top-headlines")
    Call<Headlines> getHeadlines(
            @Query("category") String category,
            @Query("country") String country
    );
}
