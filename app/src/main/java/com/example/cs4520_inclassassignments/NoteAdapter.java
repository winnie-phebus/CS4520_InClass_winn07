package com.example.cs4520_inclassassignments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<IC07Note> notes;
    private final IdeleteNoteAction dListener;

    public NoteAdapter(List<IC07Note> notes, Context context) {
        this.notes = notes;

        if (context instanceof IdeleteNoteAction) {
            this.dListener = (IdeleteNoteAction) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IdeleteNoteAction");
        }
    }

    public void setNotes(List<IC07Note> notes) {
        this.notes = notes;
        this.notifyDataSetChanged();
    }

    public List<IC07Note> getNotes() {
        return notes;
    }

    public void deleteNote(IC07Note note) {
        notes.remove(note);
        if (notes.isEmpty()) {
            notes.add(Util07.defaultNote());
        }
        this.notifyDataSetChanged();
    }

    public void addNote(IC07Note note) {
        notes.add(note);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.note_row, parent, false);

        return new ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IC07Note curr = this.getNotes().get(position);

        holder.getNoteid().setText(curr.get_id());
        holder.getNoteBody().setText(curr.getText());

        holder.getDelete().setOnClickListener(v -> dListener.deletePressedFromNote(notes.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteid;
        TextView noteBody;
        Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.noteid = itemView.findViewById(R.id.ic07_noteCV_id);
            this.noteBody = itemView.findViewById(R.id.ic07_noteCV_body);
            this.delete = itemView.findViewById(R.id.ic07_noteCV_delete);
        }

        public TextView getNoteid() {
            return noteid;
        }

        public TextView getNoteBody() {
            return noteBody;
        }

        public Button getDelete() {
            return delete;
        }
    }

    public interface IdeleteNoteAction {
        void deletePressedFromNote(IC07Note note);
    }
}
