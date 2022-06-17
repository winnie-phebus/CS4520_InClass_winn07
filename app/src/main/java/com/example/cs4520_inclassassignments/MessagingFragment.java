package com.example.cs4520_inclassassignments;

import static android.app.Activity.RESULT_OK;

import static com.example.cs4520_inclassassignments.FirebaseStorageWorker.getStorage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagingFragment extends Fragment {

    private static final String TAG = "ICO9_MSG_FRAG";
    private static final String CONVO_PARAM = "CONVERSATION";

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

    public MessagingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MessagingFragment newInstance(Conversation convo) { //, String param2) {
        MessagingFragment fragment = new MessagingFragment();
        Bundle args = new Bundle();
        args.putParcelable(CONVO_PARAM, convo);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: " + args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + getArguments());
        if (getArguments() != null) {
            conversation = getArguments().getParcelable(CONVO_PARAM);
            Log.d(TAG, "onCreate: " + conversation);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: " + conversation);
        if (getArguments() != null) {
            conversation = getArguments().getParcelable(CONVO_PARAM);
            Log.d(TAG, "onCreateView: " + conversation);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_messaging, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        chatName = v.findViewById(R.id.ic08_mess_friendName);
        recyclerView = v.findViewById(R.id.ic08_mess_recyclerView);
        newMessage = v.findViewById(R.id.ic08_mess_newMessageBody);
        sendMessage = v.findViewById(R.id.ic08_mess_sendMessage);
        return2home = v.findViewById(R.id.ic08_mess_toHome);
        sendPicture = v.findViewById(R.id.ic08_img_button);
        prevImgMsg = v.findViewById(R.id.ic09_addImg);

        Log.d(TAG, "onCreateView: " + conversation);
        chatName.setText(conversation.getChatName());

        msgs = conversation.getMessages();
        watchMessages();

        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
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
                if (InClass08Activity.hasCameraPermission(getContext())) {
                    Intent openCamera = new Intent(getContext(), CameraControllerActivity.class);
                    openCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    retrieveImg.launch(openCamera);
                } else {
                    InClass08Activity.requestPermission((Activity) getContext());
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
                    }
                    Message message = new Message(user.getDisplayName(), newMessage.getText().toString(), img);
                    conversation = InClass08Activity.addMessageToFB(
                            getContext(),
                            conversation, message);
                    updateMessages(conversation);
                    messageAdapter.notifyItemInserted(conversation.getChatters().size() - 1);

                    newMessage.setText("");
                    toggleVis(false);
                    //listener.addButtonClicked(note);
                    // TODO: make it in Adapter so that when message sender = user.getNameDisplay, it shows up as 'you'
                    // TODO: AESTHETIC - use the sender view so that text is right aligned
                }
            }
        });

        return2home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back2home = new Intent(getContext(), InClass08Activity.class);
                startActivity(back2home);
            }
        });
        return v;
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

    public void onUploadButtonPressed(Uri imageUri) {
//        Upload an image from local file....
        final int permissions = 10;
        getContext().enforceCallingOrSelfUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION, "");
        // requestPermissions(Manifest.permission.);
        // Log.d(TAG, "onUploadButtonPressed: PERMISSIONS:" + permissions);

        StorageReference storageReference = getStorage().getReference().child("images/" + user.getUid() + "/" + imageUri.getLastPathSegment());

        UploadTask uploadImage = storageReference.putFile(imageUri);
        uploadImage.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: IMGUPLOAD:", e);
                        Toast.makeText(getContext(), "Upload Failed! Try again!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // img = storageReference;
                        Toast.makeText(getContext(), "Upload successful! Check Firestorage at images/" + user.getUid(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateMessages(Conversation newmsgs) {
        this.msgs = newmsgs.getMessages();
        conversation.setMessages(msgs);
        messageAdapter.setAllMessages(msgs);
        messageAdapter.notifyDataSetChanged();
    }

    public void resultDisplay(ActivityResult result) {
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

    public interface DataManager {

    }
}