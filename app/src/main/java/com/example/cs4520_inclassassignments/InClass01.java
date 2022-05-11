package com.example.cs4520_inclassassignments;

// Winnie Phebus
// Assignment 01

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class InClass01 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class01);
    }

    private int calculateBMI(int lbs, int inHeight){
        return lbs/(inHeight * inHeight) * 703;
    }
}