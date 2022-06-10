package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {

    TextView friendName;
    RecyclerView recyclerView;
    EditText newMessage;
    Button sendMessage, return2home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        friendName = findViewById(R.id.ic08_mess_friendName);
        recyclerView = findViewById(R.id.ic08_mess_recyclerView);
        newMessage = findViewById(R.id.ic08_mess_newMessageBody);
        sendMessage = findViewById(R.id.ic08_mess_sendMessage);
        return2home = findViewById(R.id.ic08_mess_toHome);
    }
}