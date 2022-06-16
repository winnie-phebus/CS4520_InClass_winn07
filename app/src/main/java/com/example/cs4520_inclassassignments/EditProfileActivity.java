package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity {

    EditText newUsername;
    ImageView imageDisp;
    Button takePicture, leaveNoSave, saveChanges;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic09_edit_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        newUsername = findViewById(R.id.ic09_edit_newUsernameInput);
        imageDisp = findViewById(R.id.ic09_edit_profilePicDisp);
        takePicture = findViewById(R.id.ic09_edit_takePicture);
        leaveNoSave = findViewById(R.id.ic09_edit_backNoSave);
        saveChanges = findViewById(R.id.ic09_edit_save);


        newUsername.setText(user.getDisplayName());
        // TODO fix image ressource change
        // imageDisp.setImageResource(user.getPhotoUrl());

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

    }
}