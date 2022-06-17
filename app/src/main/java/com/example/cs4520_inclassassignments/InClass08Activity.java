package com.example.cs4520_inclassassignments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 08
 */
public class InClass08Activity extends AppCompatActivity {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;

    private static final String TAG = "IC08_HOME";
    private static final int PROFILE_ACT_UPDATE = 1;
    private static final int PERMISSIONS_CODE = 0x100;

    FirebaseUser user;
    RecyclerView recyclerView;
    ImageView avatar;
    TextView title, emailTV;
    Button toEditProfile, logout;
    ArrayList<Conversation> convos;
    int RESULT_OK = 1;
    int RESULT_CANCELED = 0;
    private int makeChanges;
    String newUsernameInput;
    Uri newProfilePicInput ,img;

    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private ConversationAdapter convoAdapter;
    private FirebaseFirestore db;
    private ArrayList<String> allusers;
    private MultiSelectionSpinner mySpinner;

    public static void cameraPermissionCheck(Context context) {
        //  grant permissions to camera access, and read/write on device storage.
        Boolean cameraAllowed = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        Boolean readAllowed = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Boolean writeAllowed = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (cameraAllowed && readAllowed && writeAllowed) {
            Toast.makeText(context, "All permissions granted!", Toast.LENGTH_SHORT).show();
        } else {
            InClass08Activity.requestPermission((Activity) context);
        }
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

    public static boolean hasCameraPermission(Context context) {
        return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity context) {
        context.requestPermissions(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PERMISSIONS_CODE);
    }

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

        if (user == null && getIntent() != null && getIntent().getExtras() != null) {
            user = getIntent().getParcelableExtra(AuthenticationActivity.userKey);
        }

        setTitle("Chat App");

        title = findViewById(R.id.ic08_home_title);
        emailTV = findViewById(R.id.ic08_usernameTV);
        avatar = findViewById(R.id.ic08_avatar_img);
        title.setText(String.format(getString(R.string.ic8_activity_title), user.getDisplayName()));
        emailTV.setText(String.format(getString(R.string.ic08_email_display), user.getEmail()));
        Picasso.get().load(user.getPhotoUrl()).into(avatar);

        db = FirebaseFirestore.getInstance();
        toEditProfile = findViewById(R.id.ic8_home_to_profile);
        recyclerView = findViewById(R.id.ic8_home_recyc_view);
        logout = findViewById(R.id.ic08_logout);

        allusers = new ArrayList<>();
        allusers.add(user.getDisplayName());

        /*mySpinner = findViewById(R.id.spn_items);
        mySpinner.setSelectedUsers(allusers);*/

        //findChatters();
        findNamesOfChatters();
        conversationDisplayDefault();
        Log.d(TAG, "user: " + user.toString());
        getUserConversations(user.getDisplayName());

        toEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePress();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOutAccount();
            }
        });

        // changes to be made to profile from Edit Profile page
        // default to 0
        makeChanges = (int) getIntent().getIntExtra("save_changes", 0);
        makesChangesFromEdit();
    }

    // TO-DO: make a display for the user that lets them input the new name OR/AND profile photo they'd like
    private void profilePress() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ic09_edit_profile);

        EditText newUsername;
        ImageView imageDisp;
        Button takePicture, leaveNoSave, saveChanges;
        Uri img;

        newUsername = findViewById(R.id.ic09_edit_newUsernameInput);
        imageDisp = findViewById(R.id.ic09_edit_profilePicDisp);
        takePicture = findViewById(R.id.ic09_edit_takePicture);
        leaveNoSave = findViewById(R.id.ic09_edit_backNoSave);
        saveChanges = findViewById(R.id.ic09_edit_save);

        // show username before any changes are made
        newUsername.setText(user.getDisplayName());

        // show avatar
        Picasso.get().load(user.getPhotoUrl()).into(imageDisp);

        // leave without saving edits
        leaveNoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUsernameInput = newUsername.getText().toString();
                // TODO: get picture and cast it to newProfilePicInput object
                //  newProfilePicInput = imageDisp.get
                applyChanges();
            }
        });

        ActivityResultLauncher<Intent> retrieveImg =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                resultDisplay(result, imageDisp);
                            }
                        }
                );

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InClass08Activity.hasCameraPermission(InClass08Activity.this)) {
                    Intent openCamera = new Intent(InClass08Activity.this, CameraControllerActivity.class);
                    openCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    retrieveImg.launch(openCamera);
                } else {
                    InClass08Activity.requestPermission(InClass08Activity.this);
                }
            }
        });

//        Intent toEditProfile = new Intent(this, EditProfileActivity.class);
//        //toEditProfile.putExtra("user", user);
//        // TO DO: find better way to wait for result from edit activity.
//        startActivity(toEditProfile);
//        MainActivity.showToast(InClass08Activity.this, "Not implemented yet, sorry!");
    }

    public void resultDisplay(ActivityResult result, ImageView imageView) {
        Log.d(TAG, "resultDisplay: " + result);
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;

            img = data.getExtras().getParcelable(CameraControllerActivity.IMG_KEY);
           /* this.getContentResolver().takePersistableUriPermission(img,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);*/
            Log.d(TAG, "resultDisplay: img:" + img);

            //Intent backToSender = new Intent(String.valueOf(selectedImageUri));
            //startActivity(backToSender);
            // new Intent(CameraControllerActivity.class, MessageActivity.class);
            toggleVis(true, imageView);
            Picasso.get().load(img).fit().into(imageView);
            // TODO: AESTHETIC - make ^ adjustable height w ratio
        }
    }

    private void toggleVis(boolean isVisible, ImageView imageView) {
        int vis;
        if (isVisible) {
            vis = View.VISIBLE;
        } else {
            vis = View.INVISIBLE;
        }

        imageView.clearAnimation();
        imageView.setVisibility(vis);
    }

    // apply changes to the profile if coming from EditProfile
    private void makesChangesFromEdit() {

        if (this.makeChanges == 1){
            applyChanges();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == PROFILE_ACT_UPDATE) {
            applyChanges();
        }
    }

    private void applyChanges() {
        String newUsername;
        Uri newProfilePic;

        try {
            newUsername = getIntent().getStringExtra("new username");
        } catch (NullPointerException e) {
            newUsername = user.getDisplayName();
        }

        try {
            newProfilePic = getIntent().getParcelableExtra("new profile picture");
        } catch (NullPointerException e) {
            newProfilePic = user.getPhotoUrl();
        }

        UserProfileChangeRequest builder = profileBuilder(newUsername, newProfilePic);
        userUpdate(builder);
    }

    private void logOutAccount() {
        FirebaseAuth.getInstance().signOut();
        Intent backToMainAct = new Intent(this, MainActivity.class);
        startActivity(backToMainAct);
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
                            allusers.remove(user.getDisplayName());
                            mySpinner = findViewById(R.id.spn_items);
                            mySpinner.setSelectedUsers(allusers);
                            findChatters();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void cameraPermissionCheck() {
        if (hasCameraPermission(this)) {
            enableCamera();
        } else {
            requestPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grantResults);
        switch (permsRequestCode) {

            case 200:

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;

        }

    }

    private void enableCamera() {
        Intent intent = new Intent(this, CameraControllerActivity.class);
        startActivity(intent);
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
        receivers.add(user.getDisplayName());
        FirebaseFirestore.getInstance().collection("conversations")
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
        Toast.makeText(this, "Starting a new chat", Toast.LENGTH_SHORT).show();

        String chatName = receiversToChatName(receivers);
        Message msg = new Message(user.getDisplayName(), msgText, null);
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
        if (Objects.equals(convo.getChatName(), "")) {
            MainActivity.showToast(this, "You can't open this chat! it's a tutorial!");
        } else {
            Intent openMsg = new Intent(this, MessageActivity.class);
            openMsg.putExtra("Conversation", convo);
            startActivity(openMsg);
        }
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
                .whereNotEqualTo("conversation", "/default")
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
                "press new chat and talk to your friends!",
                null);
        List<String> recps = new ArrayList<String>();
        recps.add("Tutorial");
        List<Message> msg = new ArrayList<>();
        msg.add(intro);

        convos.add(new Conversation("Get Chatting!", recps, msg));

        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        convoAdapter = new ConversationAdapter(convos, this);
        recyclerView.setAdapter(convoAdapter);
    }

    // TO-DO: this should be called after User has supplied the changes they want, then put into userUpdate()
    // https://www.codegrepper.com/code-examples/java/how+to+get+uri+of+captured+image+in+android might be helpful
    public UserProfileChangeRequest profileBuilder(String name, Uri avatar) {
        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();

        if (name != null) {
            builder.setDisplayName(name);
        }

        if (avatar != null) {
            builder.setPhotoUri(avatar);
        }

        return builder.build();
    }

    public void userUpdate(UserProfileChangeRequest profileUpdates) {
        //TO DO: fix this :) check out https://firebase.google.com/docs/auth/android/manage-users

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

}