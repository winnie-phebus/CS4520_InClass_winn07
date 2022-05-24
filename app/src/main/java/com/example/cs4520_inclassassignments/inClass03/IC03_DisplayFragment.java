package com.example.cs4520_inclassassignments.inClass03;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cs4520_inclassassignments.Profile;
import com.example.cs4520_inclassassignments.R;

/**
 * @author: Winnie Phebus
 * Assignment 03
 */
public class IC03_DisplayFragment extends Fragment {

    ImageView avatar;
    TextView name;
    TextView email;
    TextView andios;
    TextView mood;
    ImageView moodImg;

    private static final String ARG_PROFILE = "givenProfile";

    private Profile curr;

    public IC03_DisplayFragment() {
        // Required empty public constructor
    }

    // The Fragment Factory Method constructor, accidentally deleted the other form of documentation
    public static IC03_DisplayFragment newInstance(Profile curr) {
        IC03_DisplayFragment fragment = new IC03_DisplayFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROFILE, curr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curr = getArguments().getParcelable(ARG_PROFILE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ic03__display, container, false);

        avatar = rootView.findViewById(R.id.ic03_displayIV);
        name = rootView.findViewById(R.id.ic03_displayName);
        email = rootView.findViewById(R.id.ic03_DisplayEmail);
        andios = rootView.findViewById(R.id.ic03_DisplayAndIos);
        mood = rootView.findViewById(R.id.ic03_DisplayMoodAnn);
        moodImg = rootView.findViewById(R.id.ic03_DisplayMoodImg);

        updateProfile();
        return rootView;
    }

    // facilitates the transfer of profile information between the og activity and the fragment
    public void updateProfile() {
        avatar.setImageResource(curr.getAvatarId());
        name.setText("Name: " + curr.getName());
        email.setText("Email: " + curr.getEmail());
        andios.setText("I use " + curr.getAndOrIos() + "!");
        mood.setText("I am " + curr.getMood() + "!");
        moodImg.setImageResource(imgForMood(curr.getMood()));
    }

    // finds out what moodimg matches the current mood
    private int imgForMood(String mood) {
        if (mood.equals("Angry")) {
            return R.drawable.angry;
        } else if (mood.equals("Sad")) {
            return R.drawable.sad;
        } else if (mood.equals("Happy")) {
            return R.drawable.happy;
        } else { //"Awesome"
            return R.drawable.awesome;
        }
    }
}