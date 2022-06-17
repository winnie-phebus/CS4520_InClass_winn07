package com.example.cs4520_inclassassignments;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "ICO9_EDIT_PROF";

    EditText newUsername;
    ImageView imageDisp;
    Button takePicture, leaveNoSave, saveChanges;
    Uri img;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ic09_edit_profile);


        user = FirebaseAuth.getInstance().getCurrentUser();
        newUsername = findViewById(R.id.ic09_edit_newUsernameInput);
        imageDisp = findViewById(R.id.ic09_edit_profilePicDisp);
        takePicture = findViewById(R.id.ic09_edit_takePicture);
        leaveNoSave = findViewById(R.id.ic09_edit_backNoSave);
        saveChanges = findViewById(R.id.ic09_edit_save);


        newUsername.setText(user.getDisplayName());

        // show avatar
        Picasso.get().load(user.getPhotoUrl()).into(imageDisp);

        leaveNoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent(EditProfileActivity.this, InClass08Activity.class);
                returnIntent.putExtra("save_changes", 0);
                startActivity(returnIntent);
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent(EditProfileActivity.this, InClass08Activity.class);
                returnIntent.putExtra("new username",newUsername.getText().toString());
                returnIntent.putExtra("save_changes", 1);
                startActivity(returnIntent);
            }
        });

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

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InClass08Activity.hasCameraPermission(EditProfileActivity.this)) {
                    Intent openCamera = new Intent(EditProfileActivity.this, CameraControllerActivity.class);
                    openCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    retrieveImg.launch(openCamera);
                } else {
                    InClass08Activity.requestPermission(EditProfileActivity.this);
                }
            }
        });



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
            Picasso.get().load(img).fit().into(imageDisp);
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

        imageDisp.clearAnimation();
        imageDisp.setVisibility(vis);
    }
}