package com.example.cs4520_inclassassignments;

import static com.example.cs4520_inclassassignments.FirebaseStorageWorker.getStorage;

import android.Manifest;
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
 * Assignment 08
 */
public class MessageActivity extends AppCompatActivity  implements MessagingFragment.DataManager, CameraControllerActivity.DataManager{

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

        conversation = getIntent().getParcelableExtra("Conversation");
        Log.d("IC08_MA", "Conversation: " + conversation.toString());

        getSupportFragmentManager().beginTransaction()
                .addToBackStack("Message")
                .replace(R.id.ic09_msg_fragmentView, MessagingFragment.newInstance(conversation))
                .commit();
    }
}