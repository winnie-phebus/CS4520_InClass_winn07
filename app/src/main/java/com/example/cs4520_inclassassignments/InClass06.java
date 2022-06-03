package com.example.cs4520_inclassassignments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 06
 */
public class InClass06 extends AppCompatActivity implements LifecycleOwner {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class06);
        setTitle("Article Finder");

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("SelectHeadline")
                .add(R.id.ic06_fragmentContainer, new SelectHeadline())
                .commit();
    }

    public void openArticle(Headline current) {
        setTitle(current.getTitle());
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(current.getTitle())
                .replace(R.id.ic06_fragmentContainer, ArticleDisplay.newInstance(current))
                .commit();
    }
}