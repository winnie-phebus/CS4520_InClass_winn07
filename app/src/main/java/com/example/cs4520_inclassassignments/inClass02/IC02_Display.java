package com.example.cs4520_inclassassignments.inClass02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cs4520_inclassassignments.R;

// @author: Winnie Phebus
// Assignment 02

public class IC02_Display extends AppCompatActivity {
    ImageView avatar;
    TextView name;
    TextView email;
    TextView andios;
    TextView mood;
    ImageView moodImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic02_display);
        setTitle("Display Activity");

        avatar = findViewById(R.id.ic02_displayIV);
        name = findViewById(R.id.ic02_displayName);
        email = findViewById(R.id.ic02_DisplayEmail);
        andios = findViewById(R.id.ic02_DisplayAndIos);
        mood = findViewById(R.id.ic02_DisplayMoodAnn);
        moodImg = findViewById(R.id.ic02_DisplayMoodImg);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Profile curr = getIntent().getParcelableExtra(InClass02.pro_key);

            avatar.setImageResource(curr.getAvatarId());
            name.setText("Name: " + curr.getName());
            email.setText("Email: " + curr.getEmail());
            andios.setText("I use " + curr.getAndOrIos() + "!");
            mood.setText("I am " + curr.getMood() + "!");
            moodImg.setImageResource(imgForMood(curr.getMood()));
        }
    }

    // finds out what moodimg matches the current mood
    private int imgForMood(String mood) {
        if (mood.equals("Angry")) {
            return R.drawable.angry;
        } else if (mood.equals("Sad")) {
            return R.drawable.sad;
        } else if (mood.equals("Happy")) {
            return R.drawable.happy;
        } else { //"Awesome"
            return R.drawable.awesome;
        }
    }
}