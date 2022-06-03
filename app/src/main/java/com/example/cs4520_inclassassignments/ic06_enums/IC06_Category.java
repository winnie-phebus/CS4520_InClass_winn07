package com.example.cs4520_inclassassignments.ic06_enums;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 06
 */
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
        return IC06_Category.GENERAL;
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

