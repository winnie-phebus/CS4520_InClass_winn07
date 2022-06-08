package com.example.cs4520_inclassassignments;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public class RetrofitClient {
    // private static final String TAG = "GENERAL_RETROFIT";
    private static Retrofit instance = null;
    private static RetrofitAPI MY_API;

    public static synchronized Retrofit getInstance(String baseURL) {
        if (instance == null || MY_API == null) {
            Retrofit.Builder builder =
                    new Retrofit
                            .Builder()
                            .baseUrl(baseURL)
                            .client(new OkHttpClient.Builder().build())
                            .addConverterFactory(
                                    GsonConverterFactory
                                            .create(new GsonBuilder()
                                                    .create()));
            instance = builder.build();
            // Set RetrofitApi instance
            MY_API = instance.create(RetrofitAPI.class);
        }
        return instance;
    }
}
