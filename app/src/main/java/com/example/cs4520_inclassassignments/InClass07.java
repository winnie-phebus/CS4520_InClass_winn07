package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InClass07 extends AppCompatActivity implements RegisterFragment.DataManager, LoginFragment.DataManager {
    private static final String TAG = "IC07 MAIN";

    RetrofitAPI api;
    TextView titleText;
    View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class07);
        setTitle(getResources().getString(R.string.main_in_class_param, "07"));

        api = Util07.getAPI();
        titleText = findViewById(R.id.ic07_titleTV);
        fragmentContainer = findViewById(R.id.ic07_fragmentContainer);
    }

    public void postRegister(String user, String email, String password) {
        if (MainActivity.validateRegister(this, user, email, password)) {
            api.registerNewUser(user, email, password).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Success " + response.body());
                    } else {
                        responseIssueToast(response);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    errorOutToast("Request failed " + t.getMessage());
                }
            });
        } else {
            Log.d(TAG,
                    String.format("Failed valid register with vals: %s, %s, %s",
                            user, email, password));
        }
    }

    public void postLogin(String email, String password) {
        if (MainActivity.validateLogin(this, email, password)) {
            api.loginUser(email, password).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Success " + response.body());
                    } else {
                        responseIssueToast(response);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    errorOutToast("Request failed " + t.getMessage());
                }
            });
        } else {
            Log.d(TAG,
                    String.format("Failed valid login with vals: %s, %s",
                            email, password));
        }
    }

    @Override
    public void openRegisterFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("Register")
                .add(R.id.ic07_fragmentContainer, RegisterFragment.newInstance())
                .commit();
    }

    private void errorOutToast(String s) {
        MainActivity.showToast(this, s);
    }

    private void responseIssueToast(Response<String> response) {
        MainActivity.showToast(this, response.code() + ": " + response.message());
    }
}