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

import com.example.cs4520_inclassassignments.inClass07.LoginFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AthenLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AthenLoginFragment extends Fragment {
    EditText email;
    EditText password;
    Button submitLogin;
    Button moveToRegister;

    LoginFragment.DataManager dataManager;

    public AthenLoginFragment() {
        // Required empty public constructor
    }

    public static AthenLoginFragment newInstance() {
        AthenLoginFragment fragment = new AthenLoginFragment();
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
        dataManager = (AthenLoginFragment.DataManager) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rView = inflater.inflate(R.layout.fragment_login, container, false);

        email = rView.findViewById(R.id.ic08_login_username);
        password = rView.findViewById(R.id.ic08_login_pass);

        submitLogin = rView.findViewById(R.id.ic08_login_button);
        submitLogin.setOnClickListener(
                v -> dataManager.postLogin(
                        email.getText().toString(), password.getText().toString()));

        moveToRegister = rView.findViewById(R.id.ic08_send_to_reg);
        moveToRegister.setOnClickListener(v -> dataManager.openRegisterFragment());

        return rView;
    }

    public interface DataManager {
        void postLogin(String email, String password);

        void openRegisterFragment();
    }
}