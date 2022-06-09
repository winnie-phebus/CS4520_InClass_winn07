package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.cs4520_inclassassignments.inClass07.LoginFragment;
import com.example.cs4520_inclassassignments.inClass07.RegisterFragment;

public class AuthenticationActivity extends AppCompatActivity implements AuthenRegisterFragment.DataManager, AthenLoginFragment.DataManager {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic08_auth);
    }

    @Override
    public void postLogin(String email, String password) {

    }

    @Override
    public void openRegisterFragment() {

    }

    @Override
    public void postRegister(String user, String email, String password) {

    }

    @Override
    public void returnLogin() {

    }
}