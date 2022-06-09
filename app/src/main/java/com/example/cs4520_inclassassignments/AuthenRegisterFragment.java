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

import com.example.cs4520_inclassassignments.inClass07.RegisterFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AuthenRegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AuthenRegisterFragment extends Fragment {

    EditText user;
    EditText email;
    EditText password;
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
        View rView = inflater.inflate(R.layout.fragment_register, container, false);
        user = rView.findViewById(R.id.ic08_reg_username);
        email = rView.findViewById(R.id.ic08_reg_email);
        password = rView.findViewById(R.id.ic08_reg_password);

        submit = rView.findViewById(R.id.ic08_register);
        submit.setOnClickListener(v -> dataManager.postRegister(
                user.getText().toString(),
                email.getText().toString(),
                password.getText().toString()));

        backLogin = rView.findViewById(R.id.ic08_send_back_to_login);
        backLogin.setOnClickListener(v -> dataManager.returnLogin());

        // Inflate the layout for this fragment
        return rView;
    }

    public interface DataManager {
        void postRegister(String user, String email, String password);

        void returnLogin();
    }

}