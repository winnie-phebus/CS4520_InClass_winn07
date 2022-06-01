package com.example.cs4520_inclassassignments.enums;

public enum IC06_Category{

    BLANK(""),
    BUSINESS("business"),
    ENTERTAIN("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECH("technology");

    private final String displayName;

    IC06_Category(String displayName) {
        this.displayName = displayName;
    }

    // returns whatever is the correct response to represent 'empty request param'
    public static IC06_Category getDefault() {
        return IC06_Category.BLANK;
    }

    @Override
    public String toString() {
        return displayName.toUpperCase() ;
    }

    // returns the URL parameter value associated
    public String getValue() {
        return this.displayName;
    }
}

