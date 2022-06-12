package com.example.cs4520_inclassassignments;

import java.util.List;

public class Conversations {
    List<Conversation> conversations;

    public Conversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }
}
