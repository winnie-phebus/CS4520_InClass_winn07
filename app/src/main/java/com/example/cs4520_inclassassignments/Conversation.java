package com.example.cs4520_inclassassignments;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Conversation implements Parcelable {

    String chatName;
    List<String> chatters;
    List<Message> messages;

    public Conversation() {
        // for Firebase
    }

    public Conversation(String chatName, List<String> senders, List<Message> messages) {
        this.chatName = chatName;
        this.chatters = senders;
        this.messages = messages;
    }

    public Conversation(List<String> senders, List<Message> messages) {
        this.chatters = senders;
        this.messages = messages;

        this.chatName = senders.toString();
    }

    public Conversation(Parcel in) {
        this.chatName = in.readString();
        this.chatters = new ArrayList<>();
        this.messages = new ArrayList<>();
        in.readList(this.chatters, Conversation.class.getClassLoader());
        in.readList(this.messages, Conversation.class.getClassLoader());
    }


    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public List<String> getChatters() {
        return chatters;
    }

    public void setChatters(List<String> chatters) {
        this.chatters = chatters;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
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
        parcel.writeList(chatters);
        parcel.writeList(messages);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "chatName='" + chatName + '\'' +
                ", chatters=" + chatters +
                ", messages=" + messages +
                '}';
    }
}
