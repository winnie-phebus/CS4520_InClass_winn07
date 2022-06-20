package com.example.cs4520_inclassassignments;

import static com.example.cs4520_inclassassignments.FirebaseStorageWorker.getStorage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 09
 */
public class MessageActivity extends AppCompatActivity {

    final static int CAM_ACT_REQ_CODE = 50010;
    // TODO: 09- USEFUL LINK? https://firebase.google.com/docs/storage/android/upload-files
    private static final String TAG = "ICO9_MSG_A";
    TextView chatName;
    RecyclerView recyclerView;
    EditText newMessage;
    Button sendMessage, return2home, sendPicture;
    Conversation conversation;
    Uri img;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private MessageAdapter messageAdapter;
    private List<Message> msgs;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ImageView prevImgMsg;

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
        sendPicture = findViewById(R.id.ic08_img_button);
        prevImgMsg = findViewById(R.id.ic09_addImg);

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

        ActivityResultLauncher<Intent> retrieveImg =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                resultDisplay(result);
                            }
                        }
                );

        // TODO : check if this works.
        sendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InClass08Activity.hasCameraPermission(MessageActivity.this)) {
                    Intent openCamera = new Intent(MessageActivity.this, CameraControllerActivity.class);
                    openCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    retrieveImg.launch(openCamera);
                } else {
                    InClass08Activity.requestPermission(MessageActivity.this);
                }
                //startActivityForResult();
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(TextUtils.isEmpty(newMessage.getText().toString()) && img == null)) {
                    if (img != null) {
                        onUploadButtonPressed(img);
                    } else {
                        sendMessageToFB();
                    }
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

    private void sendMessageToFB() {
        Message message = new Message(user.getDisplayName(), newMessage.getText().toString(), img);
        conversation = InClass08Activity.addMessageToFB(
                MessageActivity.this,
                conversation, message);
        updateMessages(conversation);
        messageAdapter.notifyItemInserted(conversation.getChatters().size() - 1);

        newMessage.setText("");
        toggleVis(false);
    }

    public void onUploadButtonPressed(Uri imageUri) {
//        Upload an image from local file....
        final int permissions = 10;
        // enforceCallingOrSelfUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION, "");
        // requestPermissions(Manifest.permission.);
        // Log.d(TAG, "onUploadButtonPressed: PERMISSIONS:" + permissions);

        StorageReference storageReference = getStorage().getReference().child("images/" + user.getUid() + "/" + imageUri.getLastPathSegment());

        UploadTask uploadImage = storageReference.putFile(imageUri);
        uploadImage.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: IMGUPLOAD:", e);
                        Toast.makeText(MessageActivity.this, "Upload Failed! Try again!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // img = storageReference;
                        Toast.makeText(MessageActivity.this, "Upload successful! Check Firestorage at images/" + user.getUid(), Toast.LENGTH_SHORT).show();
                        sendMessageToFB();
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        prevImgMsg = findViewById(R.id.ic09_addImg);
    }

    public void resultDisplay(ActivityResult result) {
        Log.d(TAG, "resultDisplay: " + result);

        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;

            img = data.getExtras().getParcelable(CameraControllerActivity.IMG_KEY);
           /* this.getContentResolver().takePersistableUriPermission(img,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION);*/
            Log.d(TAG, "resultDisplay: img:" + img.toString());

            //Intent backToSender = new Intent(String.valueOf(selectedImageUri));
            //startActivity(backToSender);
            // new Intent(CameraControllerActivity.class, MessageActivity.class);
            toggleVis(true);
            Picasso.get().load(img).fit().into(prevImgMsg);
            // TODO: AESTHETIC - make ^ adjustable height w ratio
        }
    }

    private void toggleVis(boolean isVisible) {
        int vis;
        if (isVisible) {
            vis = View.VISIBLE;
        } else {
            vis = View.INVISIBLE;
        }

        prevImgMsg.clearAnimation();
        prevImgMsg.setVisibility(vis);
    }
}