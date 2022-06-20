package com.example.cs4520_inclassassignments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 09
 */
public class ic08_RegisterFragment extends Fragment {

    EditText firstName, lastName, user, email, password, confirmPassword;
    Button submit;
    Button backLogin;

    ic08_RegisterFragment.DataManager dataManager;

    public ic08_RegisterFragment() {
        // Required empty public constructor
    }

    public static ic08_RegisterFragment newInstance() {
        ic08_RegisterFragment fragment = new ic08_RegisterFragment();
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
        dataManager = (ic08_RegisterFragment.DataManager) context;
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
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    InputValidatorHelper helper = new InputValidatorHelper();
                    String firstNameString = firstName.getText().toString();
                    String lastNameString = lastName.getText().toString();
                    String userString = user.getText().toString();
                    String emailString = email.getText().toString();
                    String passwordString = password.getText().toString();
                    String confrimPasswordString = confirmPassword.getText().toString();


                    if (helper.isNullOrEmpty(firstNameString) ||
                            helper.isNullOrEmpty(lastNameString) ||
                            helper.isNullOrEmpty(userString) ||
                            helper.isNullOrEmpty(emailString) ||
                            helper.isNullOrEmpty(passwordString) ||
                            helper.isNullOrEmpty(confrimPasswordString)) {

                        ((AuthenticationActivity) getActivity()).makeToast("All fields most be complete.");
                    } else if (!helper.isValidEmail(emailString)) {
                        ((AuthenticationActivity) getActivity()).makeToast("Please enter valid email.");
                    } else {
                        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                            dataManager.postRegister(
                                    firstName.getText().toString(),
                                    lastName.getText().toString(),
                                    user.getText().toString(),
                                    email.getText().toString(),
                                    password.getText().toString());
                        } else {
                            ((AuthenticationActivity) getActivity()).makeToast("Passwords must match. Try again.");
                            confirmPassword.setText("");
                        }
                    }
                }
            });

        }

        backLogin = rView.findViewById(R.id.ic08_send_back_to_login);

        if (backLogin != null) {
            backLogin.setOnClickListener(v -> dataManager.returnLogin());
        }

        // Inflate the layout for this fragment
        return rView;
    }

    public interface DataManager {
        void postRegister(String firstName, String lastName, String user, String email, String password);

        void returnLogin();
    }

    public class InputValidatorHelper {

        public boolean isValidEmail(String string) {
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(string);
            return matcher.matches();
        }

        /*public boolean isValidPassword(String string, boolean allowSpecialChars){
            String PATTERN;
            if(allowSpecialChars){
                //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
                PATTERN = "^[a-zA-Z@#$%]\\w{5,19}$";
            }else{
                //PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";
                PATTERN = "^[a-zA-Z]\\w{5,19}$";
            }

            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(string);
            return matcher.matches();
        }*/

        public boolean isNullOrEmpty(String string) {
            return TextUtils.isEmpty(string);
        }
    }

}