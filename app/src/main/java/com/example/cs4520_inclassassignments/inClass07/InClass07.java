package com.example.cs4520_inclassassignments.inClass07;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cs4520_inclassassignments.MainActivity;
import com.example.cs4520_inclassassignments.R;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public class InClass07 extends AppCompatActivity implements RegisterFragment.DataManager, LoginFragment.DataManager {
    // private static final String TAG = "IC07 MAIN";
    public static final String sessionKey = "IC07 Session";

    RetrofitAPI api;
    TextView titleText;
    View fragmentContainer;
    AccessToken session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class07);
        setTitle(getResources().getString(R.string.main_in_class_param, "07"));

        //Log.d(TAG, "pre-sharedPref");
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.ic07_preferences_file), Context.MODE_PRIVATE);
        //Log.d(TAG, "post-SharedPref, pre-LoggedIn");
        String loggedIn = sharedPref.getString(getString(R.string.saved_session_key), null);

        if (loggedIn != null) {
            //Log.d(TAG, loggedIn);
            session = new AccessToken("", loggedIn);
            openNoteActivity();
        }

        api = Util07.getAPI();
        titleText = findViewById(R.id.ic07_titleTV);
        titleText.setText("Login".toUpperCase(Locale.ROOT));
        fragmentContainer = findViewById(R.id.ic07_fragmentContainer);
    }

    public void postRegister(String user, String email, String password) {
        if (MainActivity.validateRegister(this, user, email, password)) {
            api.registerNewUser(user, email, password).enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        runOnUiThread(() -> successfulAuth(response));
                    } else {
                        responseIssueToast(call, response);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccessToken> call, Throwable t) {
                    errorOutToast("Request failed " + t.getMessage());
                }
            });
        } /*else {
            //Log.d(TAG,
                    String.format("Failed valid register with vals: %s, %s, %s",
                            user, email, password));
        }*/
    }

    public void postLogin(String email, String password) {
        if (MainActivity.validateLogin(this, email, password)) {
            api.loginUser(email, password).enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        runOnUiThread(() -> successfulAuth(response));
                    } else {
                        responseIssueToast(call, response);
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, @NonNull Throwable t) {
                    errorOutToast("Request failed: " + t.getMessage());
                }
            });
        } /*else {
            //Log.d(TAG,
                    String.format("Failed valid login with vals: %s, %s",
                            email, password));
        }*/
    }

    private void successfulAuth(Response<AccessToken> response) {
        session = response.body();
        //Log.d(TAG, "Successful Authentication ");

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.ic07_preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_session_key), session.getToken());
        editor.apply();

        openNoteActivity();
    }

    private void openNoteActivity() {
        Intent openNoteActivity = new Intent(InClass07.this, NoteActivity.class);
        openNoteActivity.putExtra(sessionKey, session);
        startActivity(openNoteActivity);
    }

    @Override
    public void openRegisterFragment() {
        titleText.setText("Register".toUpperCase(Locale.ROOT));
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("Register")
                .replace(R.id.ic07_fragmentContainer, RegisterFragment.newInstance())
                .commit();
    }

    @Override
    public void returnLogin() {
        titleText.setText("Login".toUpperCase(Locale.ROOT));
        getSupportFragmentManager()
                .popBackStack();
    }

    private void errorOutToast(String s) {
        MainActivity.showToast(this, s);
    }

    private void responseIssueToast(Call<AccessToken> call, Response<AccessToken> response) {
        MainActivity.showToast(this, response.code() + ": " + response.message());
    }
}