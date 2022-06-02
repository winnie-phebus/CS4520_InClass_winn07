package com.example.cs4520_inclassassignments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cs4520_inclassassignments.enums.IC06_Category;
import com.example.cs4520_inclassassignments.enums.IC06_Country;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectHeadline#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectHeadline extends Fragment {

    private static final String TAG = "IC06";

    private TextView topTextview;
    private Spinner newsCategory;
    private Spinner newsCountry;
    private Button findNewsButton;
    private RecyclerView foundNewsDisplay;
    private RecyclerView.LayoutManager fndManager;
    private HeadlineAdapter headlineAdapter;

    private IC06_Category selectedCategory;
    private IC06_Country selectedCountry;
    private MutableLiveData<List<Headline>> articles;


    public SelectHeadline() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectHeadline newInstance() {
        SelectHeadline fragment = new SelectHeadline();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ic06_select_headline, container, false);

        initConditions(rootView);

        return rootView;

    }

    private void initConditions(View rootView) {
        topTextview = rootView.findViewById(R.id.IC06_textView);

        //defaults
        selectedCategory = IC06_Category.getDefault();
        selectedCountry = IC06_Country.getDefault();

        newsCategory = rootView.findViewById(R.id.ic06_news_category);
        ArrayAdapter<IC06_Category> adapter = new ArrayAdapter<IC06_Category>(getActivity(), android.R.layout.simple_spinner_dropdown_item, IC06_Category.values());
        newsCategory.setAdapter(adapter);
        newsCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = (IC06_Category) parent.getItemAtPosition(position);
                Log.d(TAG, "Category: " + selectedCategory.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = IC06_Category.getDefault();
            }
        });

        newsCountry = rootView.findViewById(R.id.ic06_newsCountry);
        ArrayAdapter<IC06_Country> country_adapter = new ArrayAdapter<IC06_Country>(getActivity(), android.R.layout.simple_spinner_dropdown_item, IC06_Country.values());
        newsCountry.setAdapter(country_adapter);
        newsCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = (IC06_Country) parent.getItemAtPosition(position);
                Log.d(TAG, "Country: " + selectedCountry.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountry = IC06_Country.getDefault();
            }
        });

        findNewsButton = rootView.findViewById(R.id.ic06_find_news_button);
        findNewsButton.setOnClickListener(findNews());

        articles =
                RetrofitClient.getInstance().getNews(selectedCategory, selectedCountry);
        articles.observe(getActivity(), headlines -> getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findNewsButton.setEnabled(true);
                //articles.clearAll();
                headlineAdapter = new HeadlineAdapter(articles.getValue(), getActivity());
                headlineAdapter.notifyDataSetChanged();
            }
        }));

        foundNewsDisplay = rootView.findViewById(R.id.ic06_findNewsRV);
        fndManager = new LinearLayoutManager(getActivity());
        headlineAdapter = new HeadlineAdapter(articles.getValue(), getActivity());
        foundNewsDisplay.setLayoutManager(fndManager);
        foundNewsDisplay.setAdapter(headlineAdapter);
        //headlineAdapter.notifyDataSetChanged();

    }

    private View.OnClickListener findNews() {
        return v -> {
            findNewsButton.setEnabled(false);
            articles =
                    RetrofitClient.getInstance().getNews(selectedCategory, selectedCountry);
            headlineAdapter.notifyDataSetChanged();
        };
    }
}