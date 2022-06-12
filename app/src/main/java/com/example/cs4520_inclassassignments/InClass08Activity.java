package com.example.cs4520_inclassassignments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cs4520_inclassassignments.inClass07.InClass07;
import com.example.cs4520_inclassassignments.inClass07.Util07;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.context.AttributeContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InClass08Activity extends AppCompatActivity {

    private static final String TAG = "IC08_HOME";
    FirebaseUser user;
    RecyclerView recyclerView;
    Button toEditProfile, newChat;
    ArrayList<Conversation> convos;

    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ConversationAdapter convoAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class08);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = getIntent().getParcelableExtra(AuthenticationActivity.userKey);
        }

        db = FirebaseFirestore.getInstance();
        toEditProfile = findViewById(R.id.ic8_home_to_profile);
        newChat = findViewById(R.id.ic8_home_new_chat);
        recyclerView = findViewById(R.id.ic8_home_recyc_view);


        conversationDisplayDefault();
        getUserConversations(user.getDisplayName());

        toEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showToast(InClass08Activity.this, "Profile display");
            }
        });
        newChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: change from default input and allow user to select who they want to chat with
                List<String> receivers = new ArrayList<String>();
                receivers.add("chichi");
                receivers.add("wohebus");

                // TODO: collect information for the opening message somehow
                startNewChat(receivers, ":)");
            }
        });
    }

    public void goToMessage(Conversation conversation) {
        Intent toAccount = new Intent(InClass08Activity.this, MessageActivity.class);
        toAccount.putExtra("Conversation", conversation);
        startActivity(toAccount);
    }

    public void startNewChat(List<String> receivers, String msgText) {
        // TODO: 4WINN - case where chat with participants already exists
        // TODO: 4WINN - delete/unlink 'default' conversation

        Toast.makeText(this, "Starting a new chat", Toast.LENGTH_SHORT).show();

        String chatName = receiversToChatName(receivers);
        Message msg = new Message(user.getDisplayName(), msgText);
        List<Message> msgs = new ArrayList<>();
        msgs.add(msg);

        Conversation newChat = new Conversation(chatName, receivers, msgs);
        Log.d(TAG, newChat.toString());
        convos.add(newChat);
        updateConversationDisplay();

        db.collection("conversations")
                .document(chatName)
                .set(newChat).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,
                                "FAILED adding conversation: " + e.getMessage());
                        MainActivity.showToast(
                                InClass08Activity.this,
                                "Adding new conversation to Firebase Failed.");
                    }
                });

        String path = "/"+chatName;
        Map<String, String> start = new HashMap<>();
        start.put("conversation",path);

        for (String chatter : receivers) {
            db.collection("users")
                    .document(chatter)
                    .collection("conversations")
                    .document(chatName)
                    .set(start).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,
                                    "FAILED user conversation addition: " + e.getMessage());
                            MainActivity.showToast(
                                    InClass08Activity.this,
                                    "Adding conversation to Firebase Failed for user: "+chatter);
                        }
                    });
        }

        openMessageActivity(newChat);
    }

    private void openMessageActivity(Conversation convo){
        Intent openMsg = new Intent(this, MessageActivity.class);
        openMsg.putExtra("Conversation", convo);
        startActivity(openMsg);
    }

    public String receiversToChatName(List<String> receivers){
        String recvStr = receivers.get(0);

        if (receivers.size() > 1) {
            for (int i = 1; i < receivers.size(); i++) {
                recvStr += "_" + receivers.get(i);
            }
        }

        return recvStr;
    }

    public void getUserConversations(String username){
        db.collection("users")
                .document(username)
                .collection("conversations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            convos.clear();
                            List<String> convoRefs = new ArrayList<>();
                            for (QueryDocumentSnapshot conversationRef : task.getResult()) {
                                Log.d(TAG, conversationRef.getId() + " => " + conversationRef.getData());
                                String path = conversationRef.getData().get("conversation").toString();
                                convoRefs.add(path);
                            }
                            refsToConversations(convoRefs);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void refsToConversations(List<String> convoRefs) {
        CollectionReference convos = db.collection("conversations");

        for (String path : convoRefs) {
            convos.document(path).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                    convos.add(documentSnapshot.toObject(Conversation.class));
                    updateConversationDisplay();
                }
            });
        }
    }

    private void updateConversationDisplay() {
        convoAdapter.setAllConvos(convos);
        convoAdapter.notifyDataSetChanged();
    }

    private void conversationDisplayDefault() {
        convos = new ArrayList<>();
        Message intro = new Message(
                "Default",
                "press new chat and talk to your friends!");
        List<String> recps = new ArrayList<String>();
        recps.add("Tutorial");
        List<Message> msg = new ArrayList<>();
        msg.add(intro);

        convos.add(new Conversation("Empty", recps, msg));
        // Conversations start = new Conversations(convos);

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        convoAdapter = new ConversationAdapter(convos);
        recyclerView.setAdapter(convoAdapter);
    }
}