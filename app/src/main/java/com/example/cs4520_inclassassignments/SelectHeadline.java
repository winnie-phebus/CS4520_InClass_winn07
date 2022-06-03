package com.example.cs4520_inclassassignments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4520_inclassassignments.ic06_enums.IC06_Category;
import com.example.cs4520_inclassassignments.ic06_enums.IC06_Country;

import java.util.ArrayList;
import java.util.List;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 06
 */
public class SelectHeadline extends Fragment {

    private static final String TAG = "IC06";

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

    public static SelectHeadline newInstance() {
        SelectHeadline fragment = new SelectHeadline();
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
        View rootView = inflater.inflate(R.layout.fragment_ic06_select_headline, container, false);

        initConditions(rootView);

        return rootView;
    }

    private void initConditions(View rootView) {

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
                ////Log.d(TAG, "Category: " + selectedCategory.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = IC06_Category.getDefault();
            }
        });

        newsCountry = rootView.findViewById(R.id.ic06_news_country);
        ArrayAdapter<IC06_Country> country_adapter = new ArrayAdapter<IC06_Country>(getActivity(), android.R.layout.simple_spinner_dropdown_item, IC06_Country.values());
        newsCountry.setAdapter(country_adapter);
        newsCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = (IC06_Country) parent.getItemAtPosition(position);
                ////Log.d(TAG, "Country: " + selectedCountry.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCountry = IC06_Country.getDefault();
            }
        });

        findNewsButton = rootView.findViewById(R.id.ic06_find_news_button);
        findNewsButton.setOnClickListener(findNews());

        articles = new MutableLiveData<>();
        List<Headline> startVal = new ArrayList<>();
        startVal.add(new Headline(
                "Please select a Category, Country, or both to get started.",
                "W.phebus", "", "", ""));
        articles.setValue(startVal);

        RetrofitClient.getInstance().getNews(articles, selectedCategory, selectedCountry);
        articles.observe(getActivity(), headlines -> getActivity().runOnUiThread(() -> {
            updateArticles();
            findNewsButton.setEnabled(true);
        }));

        foundNewsDisplay = rootView.findViewById(R.id.ic06_findNewsRV);
        fndManager = new LinearLayoutManager(getActivity());
        headlineAdapter = new HeadlineAdapter(articles.getValue(), getActivity());
        foundNewsDisplay.setLayoutManager(fndManager);
        foundNewsDisplay.setAdapter(headlineAdapter);
    }

    private View.OnClickListener findNews() {
        return v -> {
            findNewsButton.setEnabled(false);
            articles.getValue().clear();
            if (selectedCategory == null && selectedCountry == null) {
                MainActivity.showToast(getActivity(), "Please select a value for country and category.");
            }
            RetrofitClient.getInstance().getNews(articles, selectedCategory, selectedCountry);
        };
    }

    private void updateArticles() {
        ////Log.d(TAG, String.valueOf(articles.getValue()));
        headlineAdapter.setArticles(articles.getValue());
        ////Log.d(TAG, String.valueOf(headlineAdapter.getItemCount()));
        headlineAdapter.notifyDataSetChanged();
    }
}