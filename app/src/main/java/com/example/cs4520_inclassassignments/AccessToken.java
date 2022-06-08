package com.example.cs4520_inclassassignments;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public class AccessToken implements Parcelable {
    private final String auth;
    private final String token;

    public AccessToken(String auth, String token) {
        this.auth = auth;
        this.token = token;
    }

    protected AccessToken(Parcel in) {
        auth = in.readString();
        token = in.readString();
    }

    public static final Creator<AccessToken> CREATOR = new Creator<AccessToken>() {
        @Override
        public AccessToken createFromParcel(Parcel in) {
            return new AccessToken(in);
        }

        @Override
        public AccessToken[] newArray(int size) {
            return new AccessToken[size];
        }
    };

    public String getAuth() {
        return auth;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(auth);
        dest.writeString(token);
    }
}
