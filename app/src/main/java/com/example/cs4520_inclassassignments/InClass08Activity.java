package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

public class InClass08Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button toEditProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class08);

        recyclerView = findViewById(R.id.ic8_home_recyc_view);
        toEditProfile = findViewById(R.id.ic8_home_to_profile);
    }
}