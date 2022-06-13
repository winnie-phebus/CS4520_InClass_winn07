package com.example.cs4520_inclassassignments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 08
 */
public class ic08_LoginFragment extends Fragment {

    EditText email;
    EditText password;
    Button submitLogin;
    Button moveToRegister;

    ic08_LoginFragment.DataManager dataManager;

    public ic08_LoginFragment() {
        // Required empty public constructor
    }

    public static ic08_LoginFragment newInstance() {
        ic08_LoginFragment fragment = new ic08_LoginFragment();
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
        dataManager = (ic08_LoginFragment.DataManager) context;
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