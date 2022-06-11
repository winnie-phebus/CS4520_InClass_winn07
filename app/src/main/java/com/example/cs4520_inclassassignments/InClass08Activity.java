package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cs4520_inclassassignments.inClass07.InClass07;
import com.example.cs4520_inclassassignments.inClass07.Util07;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class InClass08Activity extends AppCompatActivity {

    FirebaseUser user;
    RecyclerView recyclerView;
    Button toEditProfile, newChat;

    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ConversationAdapter convoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class08);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = getIntent().getParcelableExtra(AuthenticationActivity.userKey);
        }

        toEditProfile = findViewById(R.id.ic8_home_to_profile);
        newChat = findViewById(R.id.ic8_home_new_chat);
        recyclerView = findViewById(R.id.ic8_home_recyc_view);


        ArrayList<Conversation> convos = new ArrayList<>();
        Message intro = new Message(
                "Default",
                "press new chat and talk to your friends!");
        List<String> recps = new ArrayList<String>();
        recps.add("Tutorial");
        List<Message> msg = new ArrayList<>();
        msg.add(intro);

        convos.add(new Conversation("Empty", recps, msg));

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        convoAdapter = new ConversationAdapter(convos);
        recyclerView.setAdapter(convoAdapter);

        toEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showToast(InClass08Activity.this, "Profile display");
            }
        });
        newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewChat();
            }
        });
    }

    public void goToMessage(Conversation conversation) {
        Intent toAccount = new Intent(InClass08Activity.this, MessageActivity.class);
        toAccount.putExtra("Conversation", conversation);
        startActivity(toAccount);
    }

    public void startNewChat() {
        Toast.makeText(this, "start a new chat", Toast.LENGTH_SHORT).show();
    }
}