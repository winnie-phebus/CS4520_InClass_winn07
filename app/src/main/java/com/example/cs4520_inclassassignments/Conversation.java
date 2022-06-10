package com.example.cs4520_inclassassignments;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Conversation implements Parcelable {

    String chatName;
    ArrayList<String> senders;
    ArrayList<Message> messages;

    public Conversation(String chatName, ArrayList<String> senders, ArrayList<Message> messages) {
        this.chatName = chatName;
        this.senders = senders;
        this.messages = messages;
    }

    public Conversation(ArrayList<String> senders, ArrayList<Message> messages) {
        this.senders = senders;
        this.messages = messages;

        this.chatName = senders.toString();
    }

    public Conversation(Parcel in) {
        this.chatName = in.readString();
    }


    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public ArrayList<String> getSenders() {
        return senders;
    }

    public void setSenders(ArrayList<String> senders) {
        this.senders = senders;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public Message getMostRecentMessage(){
        return this.messages.get(this.messages.size()-1);
    }

    public void addMessage(Message newMessage) {
        this.messages.add(newMessage);
    }



    public static final Creator<Conversation> CREATOR = new Creator<Conversation>() {
        @Override
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        @Override
        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(chatName);
        parcel.writeList(senders);
        parcel.writeList(messages);

    }
}
