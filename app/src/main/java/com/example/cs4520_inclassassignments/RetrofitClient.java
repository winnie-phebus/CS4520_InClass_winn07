package com.example.cs4520_inclassassignments;

import com.example.cs4520_inclassassignments.inClass06.DateDeserializer;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "GENERAL_RETROFIT";
    private static Retrofit instance = null;
    private static String BASE_URL;
    private static RetrofitAPI MY_API;

    public RetrofitClient(String baseURL) {
        this.BASE_URL = baseURL;
        getInstance(BASE_URL);
    }

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
