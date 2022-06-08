package com.example.cs4520_inclassassignments;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public class IC07Note {
    public String userId;
    public String text;
    public String _id;
    public String __v;

    public IC07Note(String userId, String text, String _id, String __v) {
        this.userId = userId;
        this.text = text;
        this._id = _id;
        this.__v = __v;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public String get_id() {
        return _id;
    }

    public String get__v() {
        return __v;
    }
}
