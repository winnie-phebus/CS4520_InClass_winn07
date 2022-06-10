package com.example.cs4520_inclassassignments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreviewMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreviewMessageFragment extends Fragment {

    TextView friendName, body;
    Button toMessage;

    public PreviewMessageFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PreviewMessageFragment newInstance(String param1, String param2) {
        PreviewMessageFragment fragment = new PreviewMessageFragment();
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
        View rootView =  inflater.inflate(R.layout.fragment_ic08_message_preview, container, false);
        friendName = rootView.findViewById(R.id.ic08_indivmess_name);
        body = rootView.findViewById(R.id.ic08_indivmess_body);
        toMessage = rootView.findViewById(R.id.ic08_messageCV_toMessage);

        return rootView;
    }
}