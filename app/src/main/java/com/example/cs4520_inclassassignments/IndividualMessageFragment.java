package com.example.cs4520_inclassassignments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IndividualMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IndividualMessageFragment extends Fragment {

    TextView friendName, body;

    public IndividualMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IndividualMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IndividualMessageFragment newInstance(String param1, String param2) {
        IndividualMessageFragment fragment = new IndividualMessageFragment();
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

        friendName = rootView.findViewById(R.id.ic08_indivmess_name);
        body = rootView.findViewById(R.id.ic08_indivmess_body);

        return rootView;
    }
}