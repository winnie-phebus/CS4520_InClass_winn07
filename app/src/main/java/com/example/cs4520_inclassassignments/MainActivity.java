package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    final String TAG = "Main";
    Button practiceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Activity");

        practiceButton = findViewById(R.id.practiceButton);

        practiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Practice button hit!");
                Intent toPractice = new Intent(MainActivity.this, Practice.class);
                toPractice.putExtra("MainToPractice","Alex Parkar?");
                startActivity(toPractice);
            }
        });
    }
}