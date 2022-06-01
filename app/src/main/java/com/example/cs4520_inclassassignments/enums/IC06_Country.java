package com.example.cs4520_inclassassignments.enums;

public enum IC06_Country {
    // ca, pl, us, za, nz
    DEFAULT(""),
    CA("CANADA"),
    PL("POLAND"),
    US("UNITED STATES"),
    ZA("SOUTH AFRICA"),
    NZ("NEW ZEALAND");

    private final String displayName;

    IC06_Country(String display) {
        displayName = display;
    }

    // returns whatever is the correct response to represent 'empty request param'
    public static IC06_Country getDefault() {
        return IC06_Country.DEFAULT;
    }

    @Override
    public String toString() {
        return displayName;
    }

    // returns the URL parameter value associated
    public String getValue() {
        return this.name();
    }
}
