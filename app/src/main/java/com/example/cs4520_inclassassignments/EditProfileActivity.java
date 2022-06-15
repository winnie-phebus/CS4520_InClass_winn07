package com.example.cs4520_inclassassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity {

    EditText newUsername;
    ImageView imageDisp;
    Button takePicture, leaveNoSave, saveChanges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic09_edit_profile);

        newUsername = findViewById(R.id.ic09_edit_newUsernameInput);
        imageDisp = findViewById(R.id.ic09_edit_profilePicDisp);
        takePicture = findViewById(R.id.ic09_edit_takePicture);
        leaveNoSave = findViewById(R.id.ic09_edit_backNoSave);
        saveChanges = findViewById(R.id.ic09_edit_save);

        FirebaseUser user = getIntent().getParcelableExtra("user");

        newUsername.setText(user.getDisplayName());
        // TODO fix image ressource change
        // imageDisp.setImageResource(user.getPhotoUrl());

        leaveNoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(0, returnIntent);
                finish();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("new username",newUsername.getText().toString());
                setResult(1,returnIntent);
                finish();

            }
        });

    }
}