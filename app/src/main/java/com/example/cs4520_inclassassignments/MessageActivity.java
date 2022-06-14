package com.example.cs4520_inclassassignments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 08
 */
public class MessageActivity extends AppCompatActivity {

    // TODO: 09- USEFUL LINK? https://firebase.google.com/docs/storage/android/upload-files
    private static final String TAG = "ICO8_MSG_A";
    TextView chatName;
    RecyclerView recyclerView;
    EditText newMessage;
    Button sendMessage, return2home;
    Conversation conversation;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private MessageAdapter messageAdapter;
    private List<Message> msgs;
    private FirebaseFirestore db;
    private FirebaseUser user;

    StorageReference img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        user = FirebaseAuth.getInstance().getCurrentUser();
        chatName = findViewById(R.id.ic08_mess_friendName);
        recyclerView = findViewById(R.id.ic08_mess_recyclerView);
        newMessage = findViewById(R.id.ic08_mess_newMessageBody);
        sendMessage = findViewById(R.id.ic08_mess_sendMessage);
        return2home = findViewById(R.id.ic08_mess_toHome);
        // TODO: add and implement the ImgButton

        conversation = getIntent().getParcelableExtra("Conversation");
        Log.d("IC08_MA", "Conversation: " + conversation.toString());

        chatName.setText(conversation.getChatName());

        msgs = conversation.getMessages();
        watchMessages();

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        // recyclerViewLayoutManager.stackFromEnd
        // TODO: 4WINN - 09 AESTHETIC - arrange so that messages build from the bottom instead
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        messageAdapter = new MessageAdapter(msgs);
        recyclerView.setAdapter(messageAdapter);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!newMessage.getText().toString().equals("")) {
                    Message message = new Message(user.getDisplayName(), newMessage.getText().toString(), img);
                    conversation = InClass08Activity.addMessageToFB(
                            MessageActivity.this,
                            conversation, message);
                    updateMessages(conversation);
                    messageAdapter.notifyItemInserted(conversation.getChatters().size() - 1);

                    newMessage.setText("");
                    //listener.addButtonClicked(note);
                    // TODO: make it in Adapter so that when message sender = user.getNameDisplay, it shows up as 'you'
                    // TODO: AESTHETIC - use the sender view so that text is right aligned
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

    private void watchMessages() {
        db = FirebaseFirestore.getInstance();
        db.collection("conversations")
                .document(conversation.getChatName())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, "Current data: " + snapshot.getData());
                            updateMessages(snapshot.toObject(Conversation.class));
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });
    }


    //
    public void updateMessages(Conversation newmsgs) {
        this.msgs = newmsgs.getMessages();
        conversation.setMessages(msgs);
        messageAdapter.setAllMessages(msgs);
        messageAdapter.notifyDataSetChanged();
    }
}