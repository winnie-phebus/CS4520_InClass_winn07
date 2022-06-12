package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {

    TextView chatName;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private MessageAdapter messageAdapter;
    EditText newMessage;
    Button sendMessage, return2home;

    Conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        chatName = findViewById(R.id.ic08_mess_friendName);
        recyclerView = findViewById(R.id.ic08_mess_recyclerView);
        newMessage = findViewById(R.id.ic08_mess_newMessageBody);
        sendMessage = findViewById(R.id.ic08_mess_sendMessage);
        return2home = findViewById(R.id.ic08_mess_toHome);

        conversation = getIntent().getParcelableExtra("Conversation");
        Log.d("IC08_MA", "Conversation: "+ conversation.toString());

        chatName.setText(conversation.getChatName());

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        messageAdapter = new MessageAdapter(conversation.getMessages());
        recyclerView.setAdapter(messageAdapter);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!newMessage.getText().toString().equals("")){
                    Message message = new Message("You", newMessage.getText().toString());
                    conversation.addMessage(message);
                    messageAdapter.notifyDataSetChanged();

                    newMessage.setText("");
                    //listener.addButtonClicked(note);
                    // TODO: 4WINN - make it so that messages get added to conversations in Firebase
                    // TODO: make it in Adapter so that when message sender = user.getNameDisplay, it shows up as 'you'
                }
            }
        });

        return2home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back2home = new Intent(MessageActivity.this, InClass08Activity.class);
                startActivity(back2home);
            }
        });
    }


}