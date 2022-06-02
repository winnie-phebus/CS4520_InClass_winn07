package com.example.cs4520_inclassassignments;

import java.util.ArrayList;
import java.util.List;

public class Headlines {
    private final String status;
    private final int totalResults;

    private final List<Headline> headlines;

    public Headlines(String status, int totalResults, List<Headline> headlines) {
        this.status = status;
        this.totalResults = totalResults;
        this.headlines = headlines;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Headline> getHeadlines() {
        return headlines;
    }

    @Override
    public String toString() {
        return "Headlines{" +
                "headlines=" + headlines +
                '}';
    }
}
