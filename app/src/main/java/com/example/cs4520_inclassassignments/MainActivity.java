package com.example.cs4520_inclassassignments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cs4520_inclassassignments.inClass01.InClass01;
import com.example.cs4520_inclassassignments.inClass02.InClass02;
import com.example.cs4520_inclassassignments.inClass03.InClass03;
import com.example.cs4520_inclassassignments.inClass04.InClass04;
import com.example.cs4520_inclassassignments.inClass05.InClass05;
import com.example.cs4520_inclassassignments.inClass06.InClass06;
import com.example.cs4520_inclassassignments.inClass07.InClass07;
import com.example.cs4520_inclassassignments.practice.Practice;

public class MainActivity extends AppCompatActivity {
    // final String TAG = "Main";
    private Button practiceButton;
    private Button inClass01Button;
    private Button inClass02Button;
    private Button inClass03Button;
    private Button inClass04Button;
    private Button inClass05Button;
    private Button inClass06Button;
    private Button inClass07Button;
    private Button inClass08Button;


    public static boolean nonNullInput(String input) {
        return !TextUtils.isEmpty(input);
    }

    private static boolean validEmail(Context contxt, String email) {
        boolean valid = false;
        if (!nonNullInput(email)) {
            MainActivity.showToast(contxt, "Email must be filled out.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            MainActivity.showToast(contxt, "Email must follow '@domain.com' pattern.");
        } else {
            valid = true;
        }

        return valid;
    }

    private static boolean validPassword(Context contxt, String password) {
        boolean valid = false;
        if (!nonNullInput(password)) {
            MainActivity.showToast(contxt, "Password must not remain null.");
        } else {
            valid = true;
        }
        return valid;
    }

    public static boolean validateLogin(Context contxt, String email, String password) {
        return validEmail(contxt, email) && validPassword(contxt, password);
    }

    public static boolean validateRegister(Context contxt, String username,
                                           String email, String password) {
        return nonNullInput(username)
                && validEmail(contxt, email)
                && validPassword(contxt, password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        practiceButton = findViewById(R.id.practiceButton);

        practiceButton.setOnClickListener(view -> {
            Intent toPractice = new Intent(MainActivity.this, Practice.class);
            startActivity(toPractice);
        });

        inClass01Button = findViewById(R.id.inClass01Button);
        basicButtonSetup(inClass01Button, new Intent(MainActivity.this, InClass01.class), "01");

        inClass02Button = findViewById(R.id.inClass02Button);
        basicButtonSetup(inClass02Button, new Intent(MainActivity.this, InClass02.class), "02");

        inClass03Button = findViewById(R.id.inClass03Button);
        basicButtonSetup(inClass03Button, new Intent(MainActivity.this, InClass03.class), "03");

        inClass04Button = findViewById(R.id.inClass04Button);
        basicButtonSetup(inClass04Button, new Intent(MainActivity.this, InClass04.class), "04");

        inClass05Button = findViewById(R.id.inClass05Button);
        basicButtonSetup(inClass05Button, new Intent(MainActivity.this, InClass05.class), "05");

        inClass06Button = findViewById(R.id.inClass06Button);
        basicButtonSetup(inClass06Button, new Intent(MainActivity.this, InClass06.class), "06");

        inClass07Button = findViewById(R.id.inClass07Button);
        basicButtonSetup(inClass07Button, new Intent(MainActivity.this, InClass07.class), "07");

        inClass08Button = findViewById(R.id.inClass08Button);
        basicButtonSetup(inClass08Button, new Intent(MainActivity.this, AuthenticationActivity.class), "08");
    }

    // this function simplifies the action of using a button to open a new activity
    private void basicButtonSetup(Button newButton, Intent newIntent, String num) {
        newButton.setText(getResources().getString(R.string.main_in_class_param, num));
        newButton.setOnClickListener(view -> startActivity(newIntent));
    }

    // this function simplifies the action of using a button to open a new activity
    // while also including an Extra
    private void buttonWExtra(Button newButton, Intent newIntent, String extraName) {
        newButton.setOnClickListener(view -> {
            newIntent.putExtra(extraName, "non-important");
            startActivity(newIntent);
        });
    }

    // this function simplifies Toast creation throughout the project, abstracted for fun
    public static void showToast(Context contxt, String toastMsg) {
        Toast.makeText(contxt, toastMsg, Toast.LENGTH_SHORT).show();
    }

}