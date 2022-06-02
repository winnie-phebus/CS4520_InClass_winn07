package com.example.cs4520_inclassassignments;

import java.util.ArrayList;
import java.util.List;

public class Headlines {
    private final String status;
    private final int totalResults;

    private List<Headline> articles;

    public Headlines(String status, int totalResults, List<Headline> headlines) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = headlines;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Headline> getArticles() {
        return articles;
    }

    @Override
    public String toString() {
        return "Headlines{" +
                "headlines=" + articles +
                '}';
    }
}
