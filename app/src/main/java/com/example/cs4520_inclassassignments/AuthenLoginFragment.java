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
 * Use the {@link AuthenLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthenLoginFragment extends Fragment {
    EditText email;
    EditText password;
    Button submitLogin;
    Button moveToRegister;

    AuthenLoginFragment.DataManager dataManager;

    public AuthenLoginFragment() {
        // Required empty public constructor
    }

    public static AuthenLoginFragment newInstance() {
        AuthenLoginFragment fragment = new AuthenLoginFragment();
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
        dataManager = (AuthenLoginFragment.DataManager) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rView = inflater.inflate(R.layout.fragment_ic08_login, container, false);

        email = rView.findViewById(R.id.ic08_login_username);
        password = rView.findViewById(R.id.ic08_login_pass);

        submitLogin = rView.findViewById(R.id.ic08_login_button);

        if (submitLogin != null) {
            submitLogin.setOnClickListener(
                    v -> dataManager.postLogin(
                            email.getText().toString(), password.getText().toString()));
        }


        moveToRegister = rView.findViewById(R.id.ic08_send_to_reg);

        if (moveToRegister != null) {
            moveToRegister.setOnClickListener(v -> dataManager.openRegisterFragment());
        }
        return rView;
    }

    public interface DataManager {
        void postLogin(String username, String password);

        void openRegisterFragment();
    }
}