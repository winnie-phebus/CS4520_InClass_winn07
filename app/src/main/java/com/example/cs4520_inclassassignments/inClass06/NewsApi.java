package com.example.cs4520_inclassassignments.inClass06;

import com.example.cs4520_inclassassignments.inClass06.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 06
 */
public interface NewsApi {
    String API_KEYW = "bb51263b6a024c779269a2c47b518717";

    @Headers("X-Api-Key:" + API_KEYW)
    @GET("/v2/top-headlines")
    Call<Headlines> getHeadlines(
            @Query("category") String category,
            @Query("country") String country
    );
}
