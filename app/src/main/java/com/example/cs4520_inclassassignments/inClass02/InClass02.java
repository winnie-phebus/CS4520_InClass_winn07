package com.example.cs4520_inclassassignments.inClass02;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.cs4520_inclassassignments.MainActivity;
import com.example.cs4520_inclassassignments.R;

// @author: Winnie Phebus
// Assignment 02

public class InClass02 extends AppCompatActivity {
    final static String pro_key = "currentProfile";

    Profile curr;

    EditText name;
    EditText email;
    ImageView avatar;
    Button submit;
    SeekBar moodIcator;
    ImageView moodImg;
    TextView moodAnnounce;
    RadioGroup iUseRadio;
    private String andvios;
    int avatarId;
    String moodStr;

    // for receiving the updated avatarId from SelectAvatar
    ActivityResultLauncher<Intent> startActivityForResult
            = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                avatarId = result.getData().getIntExtra("ToMain", R.drawable.select_avatar);
                avatar.setImageResource(avatarId);
            }
        }
    });

    // this is the default activity, would be "Edit Profile Activity"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class02);
        setTitle("Edit Profile Activity");
        curr = new Profile("", "", "", "", 0);

        initViews();

        // moves to Avatar activity
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSelectAvatar = new Intent(InClass02.this, IC02_SelectAvatar.class);
                startActivityForResult.launch(openSelectAvatar);
            }
        });

        iUseRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.ic2_radioAndroid) {
                    andvios = "Android";
                } else {
                    andvios = "IOS";
                }
            }
        });

        moodIcator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateMood(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // makes the Final Display Parcel, and opens Display Activity
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveProfile(curr)) {
                    Intent openDisplay = new Intent(InClass02.this, IC02_Display.class);
                    openDisplay.putExtra(pro_key, curr);
                    startActivity(openDisplay);
                }
            }
        });

    }

    // takes in an int and updates the mood accordingly
    private void updateMood(int progress) {
        if (progress == 0) {
            moodStr = "Angry";
            moodImg.setImageResource(R.drawable.angry);
        } else if (progress == 1) {
            moodStr = "Sad";
            moodImg.setImageResource(R.drawable.sad);
        } else if (progress == 2) {
            moodStr = "Happy";
            moodImg.setImageResource(R.drawable.happy);
        } else {
            moodStr = "Awesome";
            moodImg.setImageResource(R.drawable.awesome);
        }

        moodAnnounce.setText("Your current mood: " + moodStr);
    }

    // to keep the onCreate method a little more concise, may be deleted later.
    private void initViews() {
        name = findViewById(R.id.ic2_NameEdit);
        email = findViewById(R.id.ic2_emailEditText);
        avatar = findViewById(R.id.ic2_AvatarImageV);
        iUseRadio = findViewById(R.id.ic2_andorios);
        moodAnnounce = findViewById(R.id.ic2_moodIndicTextView);
        moodIcator = findViewById(R.id.ic2_MoodSeekBar);
        moodImg = findViewById(R.id.ic2_moodImageView);
        submit = findViewById(R.id.ic2_submitButton);

        // default values
        andvios = "";
        moodStr = "Happy";
        avatarId = R.drawable.select_avatar;
        avatar.setImageResource(avatarId);
    }

    // checks if inputs are valid within name and email
    private boolean validateInput(String given, int type) {
        if (type == 0 && TextUtils.isEmpty(given)) { //this would be name
            MainActivity.showToast(InClass02.this, "Invalid Input in Name. Please try again.");
            return false;
        } else if (type == 1 && (TextUtils.isEmpty(given) || !Patterns.EMAIL_ADDRESS.matcher(given).matches())) {
            MainActivity.showToast(InClass02.this, "Invalid Input in Email. Please try again.");
            return false;
        }
        return true;
    }

    // basically updates the Profile, only run after onCreate so value should never be null
    private boolean saveProfile(Profile current) {
        String nameGiven = name.getText().toString();
        String emailGiven = email.getText().toString().trim();

        if (!validateInput(nameGiven, 0) || !validateInput(emailGiven, 1)) {
            return false;
        }

        // valid name and email
        current.setName(nameGiven);
        current.setEmail(emailGiven);

        // android/ios and mood
        if (TextUtils.isEmpty(andvios)) {
            MainActivity.showToast(InClass02.this, "Please select an option for Android or IOS.");
            return false;
        }

        current.setAndOrIos(andvios);
        current.setMood(moodStr);

        // the avatarID
        current.setAvatarId(avatarId);
        return true;
    }
}