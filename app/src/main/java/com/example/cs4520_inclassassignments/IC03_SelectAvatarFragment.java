package com.example.cs4520_inclassassignments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author: Winnie Phebus
 * Assignment 03
 */
public class IC03_SelectAvatarFragment extends Fragment {
    ImageView brunette;
    ImageView blond;
    ImageView ginger;
    ImageView brunet;
    ImageView afroW;
    ImageView blackM;

    SelectDataManager dataPasser;

    public IC03_SelectAvatarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ic03__select_avatar, container, false);

        brunette = rootView.findViewById(R.id.ic03_brunetteIV);
        brunette.setOnClickListener(ivOnClick(R.drawable.avatar_f_1));

        blond = rootView.findViewById(R.id.ic03_blondIV);
        blond.setOnClickListener(ivOnClick(R.drawable.avatar_m_3));

        ginger = rootView.findViewById(R.id.ic03_gingerIV);
        ginger.setOnClickListener(ivOnClick(R.drawable.avatar_f_2));

        brunet = rootView.findViewById(R.id.ic03_brunetIV);
        brunet.setOnClickListener(ivOnClick(R.drawable.avatar_m_2));

        afroW = rootView.findViewById(R.id.ic03_afrowomanIV);
        afroW.setOnClickListener(ivOnClick(R.drawable.avatar_f_3));

        blackM = rootView.findViewById(R.id.ic03_blackmIV);
        blackM.setOnClickListener(ivOnClick(R.drawable.avatar_m_1));

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dataPasser = (SelectDataManager) context;
    }

    // internal onclick listener for all the imageview, abstracted for convenience
    private View.OnClickListener ivOnClick(int id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPasser.avatarIdUpdate(id);
            }
        };
    }

    // to allow information exchange between this fragment and its parent
    public interface SelectDataManager {
        void avatarIdUpdate(int id);
    }
}