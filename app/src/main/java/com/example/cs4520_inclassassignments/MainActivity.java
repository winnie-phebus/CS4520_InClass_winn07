package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cs4520_inclassassignments.inClass01.InClass01;
import com.example.cs4520_inclassassignments.inClass02.InClass02;
import com.example.cs4520_inclassassignments.inClass03.InClass03;
import com.example.cs4520_inclassassignments.inClass04.InClass04;
import com.example.cs4520_inclassassignments.inClass05.InClass05;
import com.example.cs4520_inclassassignments.practice.Practice;

public class MainActivity extends AppCompatActivity {
    final String TAG = "Main";
    private Button practiceButton;
    private Button inClass01Button;
    private Button inClass02Button;
    private Button inClass03Button;
    private Button inClass04Button;
    private Button inClass05Button;
    private Button inClass06Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        practiceButton = findViewById(R.id.practiceButton);

        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d(TAG, "Practice button hit!");
                Intent toPractice = new Intent(MainActivity.this, Practice.class);
                startActivity(toPractice);
            }
        });

        inClass01Button = findViewById(R.id.inClass01Button);
        basicButtonSetup(inClass01Button, new Intent(MainActivity.this, InClass01.class), "MainTo01");

        inClass02Button = findViewById(R.id.inClass02Button);
        basicButtonSetup(inClass02Button, new Intent(MainActivity.this, InClass02.class), "MainTo02");

        inClass03Button = findViewById(R.id.inClass03Button);
        basicButtonSetup(inClass03Button, new Intent(MainActivity.this, InClass03.class), "MainTo03");

        inClass04Button = findViewById(R.id.inClass04Button);
        basicButtonSetup(inClass04Button, new Intent(MainActivity.this, InClass04.class), "");

        inClass05Button = findViewById(R.id.inClass05Button);
        basicButtonSetup(inClass05Button, new Intent(MainActivity.this, InClass05.class),"");
        
        inClass06Button = findViewById(R.id.inClass06Button);
        basicButtonSetup(inClass06Button, new Intent(MainActivity.this, InClass06.class), "");
    }

    // this function simplifies the action of using a button to open a new activity
    private void basicButtonSetup(Button newButton, Intent newIntent, String extraName) {
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newIntent.putExtra(extraName, "non-important");
                startActivity(newIntent);
            }
        });
    }

    // this function simplifies Toast creation throughout the project, abstracted for fun
    public static void showToast(Context contxt, String toastMsg) {
        Toast.makeText(contxt, toastMsg, Toast.LENGTH_SHORT).show();
    }
}