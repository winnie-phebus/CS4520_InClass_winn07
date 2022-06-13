package com.example.cs4520_inclassassignments;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;


import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiSelectionSpinner extends AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener {

    private static final String TAG = "IC08_MSS";
    ArrayList<String> selectedUsers = new ArrayList<>();
    boolean[] selection = {false};
    ArrayAdapter<String> adapter;
   // private String[] usernames;

    public MultiSelectionSpinner(Context context, String[] usernames) {
        super(context);

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, usernames);
        super.setAdapter(adapter);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (selection != null && which < selection.length) {
            selection[which] = isChecked;

            adapter.clear();
            adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        Log.d(TAG,"perform click");

        //builder.set
        builder.setMultiChoiceItems(InClass08Activity.arrListString(selectedUsers), selection, this);

        Log.d(TAG, "after builder 'set mci'");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                // Do nothing
            }
        });

        Log.d(TAG, "before builder 'show");
        builder.show();
        Log.d(TAG, "after builder 'show");
        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        adapter = adapter;
    }

    public void setSelectedUsers(ArrayList<String> selectedUsers) {
        this.selectedUsers = selectedUsers;
        Log.d(TAG, selectedUsers.toString());
        selection = new boolean[this.selectedUsers.size()];
        adapter.clear();
        adapter.add("");
        Log.d(TAG, "post- Adapter work");
        Arrays.fill(selection, false);
    }

    public void setSelection(ArrayList<String> selection) {
        for (int i = 0; i < this.selection.length; i++) {
            this.selection[i] = false;
        }

        for (String sel : selection) {
            for (int j = 0; j < selectedUsers.size(); ++j) {
                if (selectedUsers.get(j).equals(sel)) {
                    this.selection[j] = true;
                }
            }
        }

        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < selectedUsers.size(); ++i) {
            if (selection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }

                foundOne = true;

                sb.append(selectedUsers.get(i));
            }
        }

        return sb.toString();
    }

    public ArrayList<String> getSelectedItems() {
        ArrayList<String> selectedItems = new ArrayList<>();

        for (int i = 0; i < selectedUsers.size(); ++i) {
            if (selection[i]) {
                selectedItems.add(selectedUsers.get(i));
            }
        }

        return selectedItems;
    }
}

