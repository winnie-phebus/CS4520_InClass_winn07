package com.example.cs4520_inclassassignments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    private ArrayList<Message> allMessages;
    private FragmentActivity fragmentActivity;

    public MessageAdapter(ArrayList<Message> allMessages, FragmentActivity fragmentActivity) {
        if(fragmentActivity != null) {
            this.allMessages = allMessages;
            this.fragmentActivity = fragmentActivity;
        } else {
            throw new RuntimeException(fragmentActivity.toString()
                    + " must be called from a Fragment");
        }
    }

    public MessageAdapter(ArrayList<Message> allMessages) {
        this.allMessages = allMessages;
    }

    public List<Message> getAllMessages() {
        return allMessages;
    }

    public void setAllMessages(ArrayList<Message> allMessages) {
        this.allMessages = allMessages;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // display/delete notes elements
        private ConstraintLayout container;
        private TextView sender, message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.container = itemView.findViewById(R.id.ic08_indivMess_MV);
            this.sender = itemView.findViewById(R.id.ic08_indivMess_name);
            this.message = itemView.findViewById(R.id.ic08_prevMess_body);

        }

        public ConstraintLayout getContainer() {
            return container;
        }

        public TextView getSender() {
            return sender;
        }

        public TextView getMessage() {
            return message;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        holder.getSender().setText(allMessages.get(position).getSender());
        holder.getMessage().setText(allMessages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return allMessages.size();
    }
}


