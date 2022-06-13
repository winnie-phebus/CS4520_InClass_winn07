package com.example.cs4520_inclassassignments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private FirebaseAuth mAuth;
    private ArrayList<String> allusers;
    private MultiSelectionSpinner mySpinner;

    @Override
    protected void onStart() {
        super.onStart();

        /*        *//*SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.ic08_preferences_file), Context.MODE_PRIVATE);
        Map<String, FirebaseUser> user = (Map<String, FirebaseUser>) sharedPref.getAll();*//*

        user = MainActivity.getSavedObjectFromPreference(
                this,
                getString(R.string.ic08_preferences_file),
                AuthenticationActivity.ic08_USER_KEY, FirebaseUser.class);

        if (user == null) {
            // openNoteActivity();
            // open Authentication??
            user = FirebaseAuth().get
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class08);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.reload();
        }

/*        if (getIntent() != null && getIntent().getExtras() != null) {
            user = getIntent().getParcelableExtra(AuthenticationActivity.userKey);
        }*/

        db = FirebaseFirestore.getInstance();
        toEditProfile = findViewById(R.id.ic8_home_to_profile);
        newChat = findViewById(R.id.ic8_home_new_chat);
        recyclerView = findViewById(R.id.ic8_home_recyc_view);


        allusers = new ArrayList<>();
        allusers.add(user.getDisplayName());

        /*mySpinner = findViewById(R.id.spn_items);
        mySpinner.setSelectedUsers(allusers);*/

        //findChatters();
        findNamesOfChatters();
        conversationDisplayDefault();
        Log.d(TAG, "user: " + user.toString());
        // getUserConversations(user.getDisplayName());

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
                // List<String> receivers = findNamesOfChatters(); // new ArrayList<String>();
                /*receivers.add("chichi");
                receivers.add("wohebus");
                receivers.add("template");*/

                startNewChat(findChatters(), ":)");
                // TODO: collect information for the opening message somehow
                //startNewChat(receivers, ":)");
            }
        });
    }

    private void findNamesOfChatters() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> userNames = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userNames.add(document.getData().get("username").toString());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            //findChatters(userNames);
                            allusers = userNames;
                            mySpinner = findViewById(R.id.spn_items);
                            mySpinner.setSelectedUsers(allusers);
                            findChatters();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    private String[] allUsersArr(){
        String[] arr = new String[allusers.size()];
        int i = 0;
        for (String str : allusers){
            arr[i] = allusers.get(i);
        }
        return arr;
    }

    public static String[] arrListString(ArrayList<String> arrList){
        Log.d(TAG, "arrListString called: "+arrList.toString());
        String[] arr = new String[arrList.size()];
        int i = 0;
        for (String str : arrList){
            arr[i] = arrList.get(i);
        }
        Log.d(TAG, "returning: "+arr.length);
        return arr;
    }

    private List<String> findChatters() {
        List<String> chatters = new ArrayList<>();
        chatters.add(user.getDisplayName());

        MultiSelectionSpinner mySpinner;

        mySpinner = findViewById(R.id.spn_items);
       // mySpinner.newAdapter(this, allUsersArr());
        // mySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, allUsersArr()));
        mySpinner.setSelectedUsers(allusers);
        chatters.addAll(mySpinner.getSelectedItems());

        return chatters;
    }

    public void goToMessage(Conversation conversation) {
        Intent toAccount = new Intent(InClass08Activity.this, MessageActivity.class);
        toAccount.putExtra("Conversation", conversation);
        startActivity(toAccount);
    }

    public void startNewChat(List<String> receivers, String msgText) {
        db.collection("conversations")
                .document(receiversToChatName(receivers))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) { // chat already exists so load it
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            openMessageActivity(document.toObject(Conversation.class));
                        } else { // doesn't exist so new chat
                            Log.d(TAG, "No such chat, creating new");
                            openNewChat(receivers, msgText);
                        }
                    }
                });
    }

    public void openNewChat(List<String> receivers, String msgText) {
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

        String path = "/" + chatName;
        Map<String, String> start = new HashMap<>();
        start.put("conversation", path);

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
                                    "Adding conversation to Firebase Failed for user: " + chatter);
                        }
                    });
        }

        openMessageActivity(newChat);
    }

    private void openMessageActivity(Conversation convo) {
        Intent openMsg = new Intent(this, MessageActivity.class);
        openMsg.putExtra("Conversation", convo);
        startActivity(openMsg);
    }

    public static Conversation addMessageToFB(Context context, Conversation convo, Message msg) {
        convo.addMessage(msg);

        FirebaseFirestore.getInstance()
                .collection("conversations")
                .document(convo.getChatName())
                .set(convo).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        MainActivity.showToast(context,
                                "Failed to add msg. -" + e.getMessage());
                    }
                });

        return convo;
    }

    public String receiversToChatName(List<String> receivers) {
        String recvStr = receivers.get(0);

        if (receivers.size() > 1) {
            for (int i = 1; i < receivers.size(); i++) {
                recvStr += "_" + receivers.get(i);
            }
        }

        return recvStr;
    }

    public void getUserConversations(String username) {
        db.collection("users")
                .document(username)
                .collection("conversations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            convos = new ArrayList<>();
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
        CollectionReference convopath = db.collection("conversations");

        for (String path : convoRefs) {
            convopath.document(path).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        convoAdapter = new ConversationAdapter(convos, this);
        recyclerView.setAdapter(convoAdapter);
    }
}