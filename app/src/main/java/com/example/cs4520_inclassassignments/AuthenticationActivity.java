package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AuthenticationActivity extends AppCompatActivity implements AuthenRegisterFragment.DataManager, AuthenLoginFragment.DataManager {

    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic08_auth);

        title = findViewById(R.id.ic08_authen_title);

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Login")
                .add(R.id.ic08_authen_fragment_container, new AuthenLoginFragment())
                .commit();

        title.setText("Login");

    }

    @Override
    public void returnLogin() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Register")
                .replace(R.id.ic08_authen_fragment_container, new AuthenLoginFragment())
                .commit();
        title.setText("Login");

    }

    // TODO: link with Database
    @Override
    public void postLogin(String username, String password) {
        Intent toMessages = new Intent(this, InClass08Activity.class);
        startActivity(toMessages);

    }

    @Override
    public void openRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Register")
                .replace(R.id.ic08_authen_fragment_container, new AuthenRegisterFragment())
                .commit();
        title.setText("Register Here");
    }

    // TODO: link with Database
    @Override
    public void postRegister(String firstName, String lastName, String user, String email, String password) {
        Intent toMessages = new Intent(this, InClass08Activity.class);
        startActivity(toMessages);

    }


}