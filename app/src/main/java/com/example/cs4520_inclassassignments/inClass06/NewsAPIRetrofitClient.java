package com.example.cs4520_inclassassignments.inClass06;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cs4520_inclassassignments.ic06_enums.IC06_Category;
import com.example.cs4520_inclassassignments.ic06_enums.IC06_Country;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 06
 */
public class NewsAPIRetrofitClient {
    private static final String TAG = "ICO6_RETROFIT";
    private static NewsAPIRetrofitClient instance = null;
    private static final String BASE_URL = "https://newsapi.org/";
    private static NewsApi myApi;

    private NewsAPIRetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(NewsApi.class);
    }

    public static synchronized NewsAPIRetrofitClient getInstance() {
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
            instance = new NewsAPIRetrofitClient();
        }
        return instance;
    }

    public void getNews(MutableLiveData<List<Headline>> results, final IC06_Category category, final IC06_Country country) {
        //Log.d(TAG, "Calling with values: " + category.getValue() + ", " + country.getValue());
        Call<Headlines> networkCall = myApi.getHeadlines(
                category.getValue(),
                country.getValue()
        );

        //Log.d(TAG, String.valueOf(networkCall));

        networkCall.enqueue(new Callback<Headlines>() {
                                @Override
                                public void onResponse(@NonNull Call<Headlines> call, @NonNull retrofit2.Response<Headlines> response) {
                                    //Log.d(TAG, response.code() + " : " + String.valueOf(response.message()));
                                    if (response.isSuccessful()) {
                                        Headlines body = response.body();
                                        if (body != null) {
                                            //Log.d(TAG, String.valueOf(response));
                                            /*//Log.d(TAG,
                                                    "Success? status: "
                                                            + body.getStatus()
                                                            + " results: "
                                                            + body.getTotalResults()
                                                            + " headlines: "
                                                            + body.getArticles());*/
                                            List<Headline> headlines = body.getArticles();
                                            results.setValue(headlines);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Headlines> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            }
        );
    }
}
