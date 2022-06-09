package com.example.cs4520_inclassassignments.inClass07;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public class IC07Profile {
    private final String _id;
    private final String name;
    private final String email;
    private final String __v;

    public IC07Profile(String _id, String name, String email, String __v) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.__v = __v;
    }

    public String getid() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getv() {
        return __v;
    }
}
