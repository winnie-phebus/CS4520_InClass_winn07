package com.example.cs4520_inclassassignments;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 08
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {
    private ArrayList<Conversation> allConvos;
    private Activity parentActivity;

    public ConversationAdapter(ArrayList<Conversation> allConvos, Activity parentActivity) {

        this.allConvos = allConvos;
        this.parentActivity = parentActivity;
/*        if(fragmentActivity != null) {
            this.allConvos = allConvos;
            this.fragmentActivity = fragmentActivity;
        } else {
            throw new RuntimeException(fragmentActivity.toString()
                    + " must be called from a Fragment");
        }*/
    }

    public ConversationAdapter(ArrayList<Conversation> allConvos) {
        this.allConvos = allConvos;
    }

    public List<Conversation> getAllConvos() {
        return allConvos;
    }

    public void setAllConvos(ArrayList<Conversation> allConvos) {
        this.allConvos = allConvos;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_ic08_message_preview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.ViewHolder holder, int position) {
        holder.getChatName().setText(allConvos.get(position).getChatName());
        Message mostRecentMessage = allConvos.get(position).getMostRecentMessage();

        String sender = mostRecentMessage.getSender();
        String text = mostRecentMessage.getMessage();
        holder.getBody().setText(sender + ": " + text);

        holder.getToConvo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Conversation conversation = allConvos.get(holder.getAdapterPosition());

                Intent toAccount = new Intent(parentActivity, MessageActivity.class);
                toAccount.putExtra("Conversation", conversation);
                parentActivity.startActivity(toAccount);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allConvos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // display/delete notes elements
        private ConstraintLayout container;
        private TextView chatName, body;
        private ImageButton toConvo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.container = itemView.findViewById(R.id.ic08_prevMess_view);
            this.chatName = itemView.findViewById(R.id.ic08_prevMess_name);
            this.body = itemView.findViewById(R.id.ic08_prevMess_body);
            this.toConvo = itemView.findViewById(R.id.ic08_prevMess_toConvo);
        }

        public ConstraintLayout getContainer() {
            return container;
        }

        public TextView getChatName() {
            return chatName;
        }

        public TextView getBody() {
            return body;
        }

        public ImageButton getToConvo() {
            return toConvo;
        }
    }
}


