package com.example.cs4520_inclassassignments.inClass07;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cs4520_inclassassignments.R;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public class ic07_LoginFragment extends Fragment {
    EditText email;
    EditText password;
    Button submitLogin;
    Button moveToRegister;

    DataManager dataManager;

    public ic07_LoginFragment() {
        // Required empty public constructor
    }

    public static ic07_LoginFragment newInstance() {
        ic07_LoginFragment fragment = new ic07_LoginFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataManager = (ic07_LoginFragment.DataManager) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rView = inflater.inflate(R.layout.fragment_login, container, false);

        email = rView.findViewById(R.id.ic07_login_username);
        password = rView.findViewById(R.id.ic07_login_pass);

        submitLogin = rView.findViewById(R.id.ic07_login_button);
        submitLogin.setOnClickListener(
                v -> dataManager.postLogin(
                        email.getText().toString(), password.getText().toString()));

        moveToRegister = rView.findViewById(R.id.ic07_send_to_reg);
        moveToRegister.setOnClickListener(v -> dataManager.openRegisterFragment());

        return rView;
    }

    public interface DataManager {
        void postLogin(String email, String password);

        void openRegisterFragment();
    }
}