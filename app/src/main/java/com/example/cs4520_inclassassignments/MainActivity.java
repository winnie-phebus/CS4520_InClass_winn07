package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cs4520_inclassassignments.inClass01.InClass01;
import com.example.cs4520_inclassassignments.practice.Practice;

public class MainActivity extends AppCompatActivity {
    final String TAG = "Main";
    Button practiceButton;
    Button inClass01Button;
    Button inClass02Button;

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
                toPractice.putExtra("MainToPractice","Alex Parkar?");
                startActivity(toPractice);
            }
        });

        inClass01Button = findViewById(R.id.inClass01Button);
        basicButtonSetup(inClass01Button, new Intent(MainActivity.this, InClass01.class), "MainTo01");

        inClass02Button = findViewById(R.id.inClass02Button);
        basicButtonSetup(inClass02Button, new Intent(MainActivity.this, InClass02.class), "MainTo02");
    }

    private void basicButtonSetup(Button newButton, Intent newIntent, String extraName){
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "Button hit");
                newIntent.putExtra(extraName,"non-important");
                startActivity(newIntent);
            }
        });
    }
}