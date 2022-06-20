package com.example.cs4520_inclassassignments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 09
 */
public class IndividualMessageFragment extends Fragment {

    TextView senderName, body;
    Message message;

    public IndividualMessageFragment() {
        // Required empty public constructor
    }

    public IndividualMessageFragment(Message message) {
        this.message = message;
    }


    public static IndividualMessageFragment newInstance(Message message) {
        IndividualMessageFragment fragment = new IndividualMessageFragment();
        Bundle args = new Bundle();
        args.putParcelable("message", message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ic08_indiv_message, container, false);

        senderName = rootView.findViewById(R.id.ic08_prevMess_name);
        body = rootView.findViewById(R.id.ic08_prevMess_body);

        senderName.setText(message.getSender());
        body.setText(message.getMessage());

        return rootView;
    }
}