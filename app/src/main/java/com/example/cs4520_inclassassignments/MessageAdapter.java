package com.example.cs4520_inclassassignments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 08
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> allMessages;

    public MessageAdapter(List<Message> allMessages) {
        this.allMessages = allMessages;
        if (this.allMessages == null) {
            Log.d("IC08_MA", "NULL ALLMESSAGES???!!");
        }
    }

    public List<Message> getAllMessages() {
        return allMessages;
    }

    public void setAllMessages(List<Message> allMessages) {
        this.allMessages = allMessages;
        this.notifyDataSetChanged();
        // TODO: adjust margins between messages
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ic08_indiv_message, parent, false);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // display/delete notes elements
        private final ConstraintLayout container;
        private final TextView sender;
        private final TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.container = itemView.findViewById(R.id.ic08_indivMess_MV);
            this.sender = itemView.findViewById(R.id.ic08_indivMess_name);
            this.message = itemView.findViewById(R.id.ic08_indivMess_body);
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
}


