package com.example.cs4520_inclassassignments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Winnie Phebus
 * Assignment 07
 */
public class NoteActivity extends AppCompatActivity implements NoteAdapter.IdeleteNoteAction {
    // private static final String TAG = "IC07_NOTE";

    TextView name;
    TextView email;
    Button logout;
    Button addNote;
    RecyclerView container;
    AccessToken session;

    private IC07Profile curr;
    private List<IC07Note> notes;
    private NoteAdapter noteAdapter;
    private RetrofitAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        name = findViewById(R.id.ic07_note_username);
        email = findViewById(R.id.ic07_note_email);

        logout = findViewById(R.id.ic07_logout);

        addNote = findViewById(R.id.ic07_add_note);

        container = findViewById(R.id.ic07_recview_container);

        if (getIntent() != null && getIntent().getExtras() != null) {
            session = getIntent().getParcelableExtra(InClass07.sessionKey);
            api = Util07.getAPI();
        }

        if (session != null) {
            api.getProfile(session.getToken()).enqueue(new Callback<IC07Profile>() {
                @Override
                public void onResponse(Call<IC07Profile> call, Response<IC07Profile> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            curr = response.body();
                            assert curr != null;
                            name.setText(curr.getName());
                            email.setText(curr.getEmail());
                            toggleButtons(true);
                            recyclerViewUpdate();
                        });
                    } else {
                        responseIssueToast(call, response);
                    }
                }

                @Override
                public void onFailure(Call<IC07Profile> call, Throwable t) {
                    errorOutToast("Request failed " + t.getMessage());
                }
            });
            toggleButtons(false);

            logout.setOnClickListener(v -> logoutUserPress());
            addNote.setOnClickListener(v -> newNotePress());

            recyclerViewUpdate();
        }
    }

    public void responseIssueToast(Call<IC07Profile> call, Response<IC07Profile> response) {
        if (response.body() != null) {
            //Log.d(TAG, "Failure: " + response.body());
            MainActivity.showToast(this, response.code() + ": " + response.message());
        } /*else {
            //Log.d(TAG, "Response " + response
                    + " received for call:" + call.toString()
                    + " try something different?");
        }*/
    }

    private void toggleButtons(boolean enabled) {
        logout.setEnabled(enabled);
        addNote.setEnabled(enabled);
    }

    private void recyclerViewUpdate() {
        if (curr == null) {
            recyclerViewInit();
        } else {
            toggleButtons(false);
            api.getAllNotes(session.getToken()).enqueue(new Callback<IC07Notes>() {
                @Override
                public void onResponse(Call<IC07Notes> call, Response<IC07Notes> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            notes.clear();
                            notes.addAll(response.body().getNotes());
                            updateNotes();
                            toggleButtons(true);
                        });
                    } else {
                        errorOutToast(response.code() + ": " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<IC07Notes> call, Throwable t) {
                    errorOutToast(t.getMessage());
                }
            });
        }
    }

    private void updateNotes() {
        noteAdapter.setNotes(notes);
        noteAdapter.notifyDataSetChanged();
    }

    private void recyclerViewInit() {
        List<IC07Note> startVal = new ArrayList<>();
        startVal.add(Util07.defaultNote());
        notes = startVal;
        container = findViewById(R.id.ic07_recview_container);
        container.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(notes, this);
        container.setAdapter(noteAdapter);
    }

    private void newNotePress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Note");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String noteBody = input.getText().toString();
            newNote(noteBody);
            //Log.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void newNote(String noteBody) {
        api.postNewNote(session.getToken(), noteBody).enqueue(new Callback<IC07Note>() {
            @Override
            public void onResponse(Call<IC07Note> call, Response<IC07Note> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        noteAdapter.addNote(response.body());
                        recyclerViewUpdate();
                        noteAdapter.notifyDataSetChanged();
                    });
                } else {
                    errorOutToast(response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<IC07Note> call, Throwable t) {
                errorOutToast("Request failed with: " + t.getMessage());
            }
        });
    }

    private void logoutUserPress() {
        api.logoutUser();
        session = null;

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.ic07_preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_session_key), null);
        //editor.remove(getString(R.string.saved_session_key));
        editor.apply();
        //Log.d(TAG, "Successful SharedPreferences update?");

        Intent backToIC07 = new Intent(NoteActivity.this, InClass07.class);
        startActivity(backToIC07);
    }

    private void errorOutToast(String s) {
        //Log.d(TAG, s);
        MainActivity.showToast(this, s);
        toggleButtons(true);
    }

    @Override
    public void deletePressedFromNote(IC07Note note) {
        //Log.d(TAG, "deleting note: " + note.get_id());
        api.deleteNote(session.getToken(), note.get_id()).enqueue(new Callback<IC07Note>() {
            @Override
            public void onResponse(Call<IC07Note> call, Response<IC07Note> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        MainActivity.showToast(
                                NoteActivity.this,
                                "Delete successful.");
                        noteAdapter.deleteNote(note);
                        noteAdapter.notifyDataSetChanged();
                    });
                } else {
                    errorOutToast(response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<IC07Note> call, Throwable t) {
                errorOutToast("Request failed with: " + t.getMessage());
            }
        });
    }
}