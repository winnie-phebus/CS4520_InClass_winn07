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
public class RegisterFragment extends Fragment {
    EditText user;
    EditText email;
    EditText password;
    Button submit;
    Button backLogin;

    DataManager dataManager;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        dataManager = (DataManager) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rView = inflater.inflate(R.layout.fragment_register, container, false);
        user = rView.findViewById(R.id.ic07_reg_username);
        email = rView.findViewById(R.id.ic07_reg_email);
        password = rView.findViewById(R.id.ic07_reg_password);

        submit = rView.findViewById(R.id.ic07_register);
        submit.setOnClickListener(v -> dataManager.postRegister(
                user.getText().toString(),
                email.getText().toString(),
                password.getText().toString()));

        backLogin = rView.findViewById(R.id.ic07_send_back_to_login);
        backLogin.setOnClickListener(v -> dataManager.returnLogin());

        // Inflate the layout for this fragment
        return rView;
    }

    public interface DataManager {
        void postRegister(String user, String email, String password);

        void returnLogin();
    }
}