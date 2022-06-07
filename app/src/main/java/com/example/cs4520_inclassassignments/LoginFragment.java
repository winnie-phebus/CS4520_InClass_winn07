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
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    EditText email;
    EditText password;
    Button submitLogin;
    Button moveToRegister;
    
    DataManager dataManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataManager = (LoginFragment.DataManager) context;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        email = container.findViewById(R.id.ic07_login_username);
        password = container.findViewById(R.id.ic07_login_pass);
        
        submitLogin = container.findViewById(R.id.ic07_login);
        submitLogin.setOnClickListener(
                v -> dataManager.postLogin(
                        email.getText().toString(), password.getText().toString()));
        
        moveToRegister = container.findViewById(R.id.ic07_send_to_reg);
        moveToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.openRegisterFragment();
            }
        });
        
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    
    public interface DataManager{
        void postLogin(String email, String password);

        void openRegisterFragment();
    }
}