package com.example.cs4520_inclassassignments;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cs4520_inclassassignments.enums.IC06_Category;
import com.example.cs4520_inclassassignments.enums.IC06_Country;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "ICO6_RETROFIT";
    private static RetrofitClient instance = null;
    private static final String BASE_URL = "https://newsapi.org/";
    private static NewsApi myApi;

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(NewsApi.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null || myApi == null) {
            Retrofit.Builder builder =
                    new Retrofit
                            .Builder()
                            .baseUrl(BASE_URL)
                            .client(new OkHttpClient.Builder().build())
                            .addConverterFactory(
                                    GsonConverterFactory
                                            .create(new GsonBuilder()
                                                    .registerTypeAdapter(
                                                            Date.class,
                                                            new DateDeserializer())
                                                    .create()));
            // Set NewsApi instance
            myApi = builder.build().create(NewsApi.class);
            instance = new RetrofitClient();
        }
        return instance;
    }

    public List<Headline> getNews(final IC06_Category category, final IC06_Country country) {
        Log.d(TAG, "Calling with values: " + category.getValue() + ", " + country.getValue());
        Call<Headlines> networkCall = myApi.getHeadlines(
                category.getValue(),
                country.getValue()
        );

        final List<Headline>[] results = new List[]{null};

        networkCall.enqueue(new Callback<Headlines>() {
                                @Override
                                public void onResponse(@NonNull Call<Headlines> call, @NonNull retrofit2.Response<Headlines> response) {
                                    Log.d(TAG, response.code() + " : " + String.valueOf(response.message()));
                                    if (response.isSuccessful()) {
                                        Headlines body = response.body();
                                        if (body != null) {
                                            Log.d(TAG,
                                                    "Success? status: "
                                                            + body.getStatus()
                                                            + " results: "
                                                            + body.getTotalResults()
                                                            + " headlines: "
                                                            + body.getHeadlines());
                                            results[0] = body.getHeadlines();
                                        }
                                    } else {
                                        Log.d(TAG, response.code() + " : " + String.valueOf(response.message()));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Headlines> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            }
        );

        return results[0];
    }

/*    public Api getMyApi() {
        return myApi;
    }*/
}
