package com.example.cs4520_inclassassignments;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

    String sender, message;

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    protected Message(Parcel in) {
        sender = in.readString();
        message = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sender);
        parcel.writeString(message);

    }
}

