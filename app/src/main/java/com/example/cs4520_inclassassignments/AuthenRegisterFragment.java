package com.example.cs4520_inclassassignments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthenRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthenRegisterFragment extends Fragment {

    EditText firstName, lastName, user, email, password, confirmPassword;
    Button submit;
    Button backLogin;

    AuthenRegisterFragment.DataManager dataManager;

    public AuthenRegisterFragment() {
        // Required empty public constructor
    }

    public static AuthenRegisterFragment newInstance() {
        AuthenRegisterFragment fragment = new AuthenRegisterFragment();
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
        dataManager = (AuthenRegisterFragment.DataManager) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rView = inflater.inflate(R.layout.fragment_ic08_register, container, false);

        firstName = rView.findViewById(R.id.ic08_reg_firstName);
        lastName = rView.findViewById(R.id.ic08_reg_lastName);
        user = rView.findViewById(R.id.ic08_reg_username);
        email = rView.findViewById(R.id.ic08_reg_email);
        password = rView.findViewById(R.id.ic08_reg_password);
        confirmPassword = rView.findViewById(R.id.ic08_reg_confrimPassword);

        submit = rView.findViewById(R.id.ic08_register);

        if (submit != null) {
            submit.setOnClickListener(v -> dataManager.postRegister(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    user.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString()));
        }

        backLogin = rView.findViewById(R.id.ic08_send_back_to_login);

        if (backLogin != null){
            backLogin.setOnClickListener(v -> dataManager.returnLogin());
        }

        // Inflate the layout for this fragment
        return rView;
    }

    public interface DataManager {
        void postRegister(String firstName, String lastName, String user, String email, String password);

        void returnLogin();
    }

}