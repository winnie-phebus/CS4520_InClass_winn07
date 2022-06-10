package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class InClass08Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button toEditProfile, newChat;

    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ConversationAdapter convoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class08);

        recyclerView = findViewById(R.id.ic8_home_recyc_view);
        toEditProfile = findViewById(R.id.ic8_home_to_profile);
        newChat = findViewById(R.id.ic8_home_new_chat);
    }

    public void goToMessage(Conversation conversation) {
        Intent toAccount = new Intent(InClass08Activity.this, MessageActivity.class);
        toAccount.putExtra("Conversation", conversation);
        startActivity(toAccount);

    }

    public void startNewChat(){
        Toast.makeText(this, "start a new chat", Toast.LENGTH_SHORT).show();
    }
}