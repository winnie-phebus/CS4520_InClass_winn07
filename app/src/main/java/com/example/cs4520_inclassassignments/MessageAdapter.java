package com.example.cs4520_inclassassignments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 09
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
        Message current = allMessages.get(position);

        holder.getSender().setText(current.getSender());
        holder.getMessage().setText(current.getMessage());

        if (current.getImg() != null) {
            Picasso.get().load(current.getImg()).into(holder.getImg());
            holder.getImg().setVisibility(View.VISIBLE);
        }
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
        private final ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.container = itemView.findViewById(R.id.ic08_indivMess_MV);
            this.sender = itemView.findViewById(R.id.ic08_indivMess_name);
            this.message = itemView.findViewById(R.id.ic08_indivMess_body);
            this.img = itemView.findViewById(R.id.ic09_msgImg);
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

        public ImageView getImg() {
            return img;
        }
    }
}


