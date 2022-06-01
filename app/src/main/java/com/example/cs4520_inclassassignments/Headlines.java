package com.example.cs4520_inclassassignments;

import java.util.ArrayList;

public class Headlines {

    private ArrayList<Headline> headlines;

    public Headlines() {
    }

    public Headlines(ArrayList<Headline> headlines) {
        this.headlines = headlines;
    }

    public ArrayList<Headline> getHeadlines() {
        return headlines;
    }

    public void setHeadlines(ArrayList<Headline> headlines) {
        this.headlines = headlines;
    }

    @Override
    public String toString() {
        return "Headlines{" +
                "headlines=" + headlines +
                '}';
    }
}
