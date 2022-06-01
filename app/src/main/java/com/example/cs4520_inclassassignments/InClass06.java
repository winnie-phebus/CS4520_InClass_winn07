package com.example.cs4520_inclassassignments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs4520_inclassassignments.enums.IC06_Category;
import com.example.cs4520_inclassassignments.enums.IC06_Country;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InClass06 extends AppCompatActivity {

    private static final String TAG = "IC06";
    private final OkHttpClient client = new OkHttpClient();

    private TextView topTextview;
    private Spinner newsCategory;
    private Spinner newsCountry;
    private Button findNewsButton;
    private ListView listView;

    private String apikey = "f777477991854b12a9a2f60117e85e34";

    private IC06_Category selectedCategory;
    private IC06_Country selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class06);
        setTitle("Article Finder");

        initConditions();
    }

    // binds the views to their corresponding variables
    private void initConditions() {
        topTextview = findViewById(R.id.IC06_textView);

        newsCategory = findViewById(R.id.ic06_news_category);
        ArrayAdapter<IC06_Category> adapter = new ArrayAdapter<IC06_Category>(this, android.R.layout.simple_spinner_dropdown_item, IC06_Category.values());
        newsCategory.setAdapter(adapter);
        newsCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (IC06_Category) parent.getItemAtPosition(position);
                Log.d(TAG, "Category: " + selectedCategory.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = IC06_Category.getDefault();
            }
        });

        newsCountry = findViewById(R.id.ic06_news_country);
        ArrayAdapter<IC06_Country> country_adapter = new ArrayAdapter<IC06_Country>(this, android.R.layout.simple_spinner_dropdown_item, IC06_Country.values());
        newsCountry.setAdapter(country_adapter);
        newsCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = (IC06_Country) parent.getItemAtPosition(position);
                Log.d(TAG, "Country: " + selectedCountry.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountry = IC06_Country.getDefault();
            }
        });

        findNewsButton = findViewById(R.id.ic06_find_news_button);
        findNewsButton.setOnClickListener(findNews());

        listView = findViewById(R.id.ic06_news_listview);
    }

    private View.OnClickListener findNews() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNews();
                findNewsButton.setEnabled(false);
            }
        };
    }

    // trying to use a simpler wrapper library for the Request, having dependency issues??
    private void acquireNews(){
       /* NewsApiClient newsApiClient = new NewsApiClient(apikey);

        // /v2/top-headlines
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .q("bitcoin")
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        System.out.println(response.getArticles().get(0).getTitle());
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }
                }
        );*/
    }
    private void getNews() {
        String baseNewsAPIURL = "https://newsapi.org/v2/top-headlines?";

        HttpUrl finUrl = HttpUrl.parse(baseNewsAPIURL).newBuilder()
                .addQueryParameter("category", selectedCategory.getValue())
                .addQueryParameter("country", selectedCountry.getValue())
                .addQueryParameter("apiKey", apikey)
                .build();

        Log.d(TAG, finUrl.toString());

        Request request = new Request.Builder()
                .url(finUrl)
                .build();

        Log.d(TAG, request.toString());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Gson gsonData = new Gson();

                    gsonData.fromJson(response.body().charStream(), Headlines.class);
                    Log.d(TAG, "Success");
                } else {
                    Log.d(TAG, String.valueOf(response.message()));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findNewsButton.setEnabled(true);
                    }
                });
            }
        });
    }
}