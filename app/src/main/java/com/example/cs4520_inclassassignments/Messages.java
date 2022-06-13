package com.example.cs4520_inclassassignments;

import java.util.List;

public class Messages {
    List<Message> messages;

    public Messages() {
    }

    public Messages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
