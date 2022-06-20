package com.example.cs4520_inclassassignments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 09
 */
public class PreviewMessageFragment extends Fragment {

    TextView chatName, body;
    ImageButton toMessage;

    Conversation conversation;

    public PreviewMessageFragment() {
        // Required empty public constructor
    }


    public static PreviewMessageFragment newInstance(Conversation conversation) {
        PreviewMessageFragment fragment = new PreviewMessageFragment();
        Bundle args = new Bundle();
        args.putParcelable("Conversation", conversation);
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
        View rootView = inflater.inflate(R.layout.fragment_ic08_message_preview, container, false);
        chatName = rootView.findViewById(R.id.ic08_prevMess_name);
        body = rootView.findViewById(R.id.ic08_prevMess_body);
        toMessage = rootView.findViewById(R.id.ic08_messageCV_toMessage);

        chatName.setText(conversation.getChatName());

        Message lastMessage = conversation.getMostRecentMessage();
        String lastSender = lastMessage.getSender();
        String lastText = lastMessage.getMessage();
        body.setText(lastSender + ": " + lastText);
        toMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InClass08Activity) getActivity()).goToMessage(conversation);
            }
        });

        return rootView;
    }
}