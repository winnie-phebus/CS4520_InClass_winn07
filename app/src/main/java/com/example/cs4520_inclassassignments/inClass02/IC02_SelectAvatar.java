package com.example.cs4520_inclassassignments.inClass02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cs4520_inclassassignments.R;

// @author: Winnie Phebus
// Assignment 02

public class IC02_SelectAvatar extends AppCompatActivity {
    ImageView brunette;
    ImageView blond;
    ImageView ginger;
    ImageView brunet;
    ImageView afroW;
    ImageView blackM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic02_select_avatar);
        setTitle("Select Avatar");

        brunette = findViewById(R.id.ic02_brunetteIV);
        brunette.setOnClickListener(ivOnClick(R.drawable.avatar_f_1));

        blond = findViewById(R.id.ic02_blondIV);
        blond.setOnClickListener(ivOnClick(R.drawable.avatar_m_3));

        ginger = findViewById(R.id.ic02_gingerIV);
        ginger.setOnClickListener(ivOnClick(R.drawable.avatar_f_2));

        brunet = findViewById(R.id.ic02_brunetIV);
        brunet.setOnClickListener(ivOnClick(R.drawable.avatar_m_2));

        afroW = findViewById(R.id.ic02_afrowomanIV);
        afroW.setOnClickListener(ivOnClick(R.drawable.avatar_f_3));

        blackM = findViewById(R.id.ic02_blackmIV);
        blackM.setOnClickListener(ivOnClick(R.drawable.avatar_m_1));
    }

    // internal onclick listener for all the imageview, abstracted for convenience
    private View.OnClickListener ivOnClick(int id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToMain = new Intent();
                backToMain.putExtra("ToMain", id);
                setResult(RESULT_OK, backToMain);
                finish();
            }
        };
    }
}