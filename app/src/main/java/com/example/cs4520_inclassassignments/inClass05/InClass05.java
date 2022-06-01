package com.example.cs4520_inclassassignments.inClass05;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs4520_inclassassignments.MainActivity;
import com.example.cs4520_inclassassignments.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Winnie Phebus
 * Assignment 05
 */

public class InClass05 extends AppCompatActivity {
    private static final String IC05 = "IC05";
    private final OkHttpClient client = new OkHttpClient();
    String[] keywords;
    EditText keywordInput;
    Button goButton;
    ImageView imDisplay;
    ProgressBar pBar;
    TextView loadIndic;
    ImageView prevButton;
    ImageView nextButton;
    private int imgIdx;
    private HttpUrl imAPI;
    private String currentKW;

    private String[] imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class05);
        setTitle("Image Search");

        initConditions();
        goButton.setEnabled(false);

        toggleLoadingState(true, " keywords");
        getKeywords();

        //goButton = findViewById(R.id.ic05_goButton);
        goButton.setOnClickListener(goButtonPress());
    }

    private void initConditions() {
        Log.d(IC05, "Init Conditions");
        currentKW = "";
        keywordInput = findViewById(R.id.ic05_searchKeyword);

        imDisplay = findViewById(R.id.ic05_imageDisplay);

        pBar = findViewById(R.id.ic05_progressBar);
        loadIndic = findViewById(R.id.ic05_loadingTV);

        nextButton = findViewById(R.id.ic05_nextButton);
        nextButton.setEnabled(false);
        nextButton.setOnClickListener(arrowButtonPress(nextButton, true));

        prevButton = findViewById(R.id.ic05_prevButton);
        prevButton.setEnabled(false);
        prevButton.setOnClickListener(arrowButtonPress(prevButton, false));

        goButton = findViewById(R.id.ic05_goButton);
    }

    private View.OnClickListener arrowButtonPress(ImageView b, boolean downloadNext) {
        return v -> {
            Log.d(IC05, String.valueOf(b.isEnabled()));
            if (imgs == null) {
                MainActivity.showToast(
                        InClass05.this,
                        "No Images Loaded. Please press 'GO'.");
                return;
            }

            if (b.isEnabled()) {
                int cap = imgs.length;
                if (downloadNext) {
                    toggleLoadingState(true, " next");
                    imgIdx = (imgIdx + 1) % cap;
                    updateImgDisplay();
                } else {
                    toggleLoadingState(true, " prev");
                    int temp = (imgIdx - 1);
                    imgIdx = (temp % cap);

                    if (imgIdx < 0) {
                        imgIdx = cap + imgIdx;
                    }

                    updateImgDisplay();
                }
            } else {
                MainActivity.showToast(
                        InClass05.this,
                        "Button currently disabled. Input keyword.");
            }
        };
    }

    private void updateImgURL() {
        String imagesBaseURL = "http://dev.sakibnm.space/apis/images/retrieve";
        imAPI = HttpUrl.parse(imagesBaseURL).newBuilder().
                addQueryParameter("keyword", currentKW).build();
    }

    private View.OnClickListener goButtonPress() {
        Log.d(IC05, "go button pressed");
        return v -> {
            currentKW = keywordInput.getText().toString();
            imgs = new String[]{};
            if (!TextUtils.isEmpty(currentKW)) {
                toggleLoadingState(true, " images for " + currentKW);
                updateImgURL();
                Request req = new Request.Builder()
                        .url(imAPI)
                        .build();
                Log.d(IC05, "Sending request with " + currentKW);
                client.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(IC05, e.getStackTrace().toString());
                        runOnUiThread(
                                errToast("Issue loading images, check Internet connection?"));
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Log.d(IC05, "Response gotten.");
                        if (response.isSuccessful() && response.body() != null) {
                            imgs = response.body().string().split("\n");
                            Log.d(IC05, imgs[0]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imgIdx = 0;
                                    toggleLoadingState(false, "");
                                    updateImgDisplay();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MainActivity.showToast(InClass05.this,
                                            "Invalid input. Keyword not found.");
                                    toggleLoadingState(false, "");
                                    currentKW = "";
                                }
                            });
                        }
                    }
                });
            } else {
                runOnUiThread(errToast("Invalid input, keyword searchbar can not be empty."));
            }
        };
    }

    // just for convenience
    public Runnable errToast(String msg) {
        return new Runnable() {
            @Override
            public void run() {
                MainActivity.showToast(InClass05.this, msg);
            }
        };
    }

    private void updateImgDisplay() {
        Log.d(IC05, "trying to update IMG");
        if (imgs != null && imgs[imgIdx] != null && !TextUtils.isEmpty(imgs[imgIdx])) {
            Picasso.get().load(imgs[imgIdx]).into(imDisplay);
            Log.d(IC05, "imgdisplay update success? : " + imgs[imgIdx]);
        } else {
            imDisplay.setImageDrawable(null);
            MainActivity.showToast(
                    InClass05.this,
                    "No Images found.");
        }

        toggleLoadingState(false, "");
    }

    // acquires the list of Keywords from the Keywords API
    private void getKeywords() {
        Log.d(IC05, "Trying for keywords.");
        String keywordUrl = "http://dev.sakibnm.space/apis/images/keywords";
        Request request = new Request.Builder()
                .url(keywordUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(errToast("Issue loading keywords, check Internet connection?"));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        String body = responseBody.string();
                        runOnUiThread(() -> {
                            Log.d("IC05", "keywords got");
                            keywords = body.split(",");
                            toggleLoadingState(false, "");
                        });
                    }
                } else {
                    runOnUiThread(errToast(
                            "Issue loading keywords, check Internet connection?"));
                }
            }
        });

        Log.d(IC05, "leaving getKeywords.");
    }

    private void toggleLoadingState(boolean loading, String loadAppend) {
        Log.d(IC05, "toggling to loading: " + loading);
        int hide = View.GONE;
        int show = View.VISIBLE;

        if (loading) {
            imDisplay.setVisibility(hide);
            pBar.setVisibility(show);

            loadIndic.setVisibility(show);
            loadIndic.setText("Loading" + loadAppend + "...");
        } else {
            new CountDownTimer(500, 10) {

                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    imDisplay.setVisibility(show);
                    pBar.setVisibility(hide);
                    loadIndic.setVisibility(hide);
                }
            }.start();
        }
        Log.d(IC05, "Visibilities(img,pbar,tv):" + imDisplay.getVisibility() + pBar.getVisibility() + loadIndic.getVisibility());

        goButton.setEnabled(!loading);
        nextButton.setEnabled(!loading);
        prevButton.setEnabled(!loading);
    }
}