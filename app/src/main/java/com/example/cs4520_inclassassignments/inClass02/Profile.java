package com.example.cs4520_inclassassignments.inClass02;

// @author: Winnie Phebus
// Assignment 02

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private String name;
    private String email;
    private String andOrIos;
    private String mood;
    private int avatarId;

    // Constructor :)
    public Profile(String name, String email, String andOrIos, String mood, int avatarId) {
        this.name = name;
        this.email = email;
        this.andOrIos = andOrIos;
        this.mood = mood;
        this.avatarId = avatarId;
    }

    // the parcel builder / constructor, compared to the Class Public one
    protected Profile(Parcel in) {
        name = in.readString();
        email = in.readString();
        andOrIos = in.readString();
        mood = in.readString();
        avatarId = in.readInt();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(andOrIos);
        dest.writeString(mood);
        dest.writeInt(avatarId);
    }

    // Getters and Setters beyond this point
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAndOrIos() {
        return andOrIos;
    }

    public void setAndOrIos(String andOrIos) {
        this.andOrIos = andOrIos;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }
}
