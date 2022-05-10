package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Practice extends AppCompatActivity {
    final String TAG = "Practice";
    Button logcatButton;
    Button toastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        logcatButton = findViewById(R.id.logcatButton);
        logcatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Practice!Practice!Practice!");
            }
        });

        toastButton = findViewById(R.id.practiceToastButton);
        toastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Practice.this, "Now push to GitHub and Submit!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}