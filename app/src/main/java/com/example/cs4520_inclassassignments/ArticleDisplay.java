package com.example.cs4520_inclassassignments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleDisplay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleDisplay extends Fragment {

    //private final Headline headline;
    TextView articleName, author, date, description;
    ImageView image;

    private static final String ARG_HEADLINE = "givenHeadline";

    private Headline headline;


    public ArticleDisplay() {
        // Required empty public constructor
    }

    public static ArticleDisplay newInstance(Headline headline) {
        ArticleDisplay fragment = new ArticleDisplay();
        Bundle args = new Bundle();
        args.putParcelable(ARG_HEADLINE, (Parcelable) headline);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // default article

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ic06_article_display, container, false);

        articleName = rootView.findViewById(R.id.ic06_textView_articleName);
        author = rootView.findViewById(R.id.ic06_textView_author);
        date = rootView.findViewById(R.id.ic06_textView_dateTime);
        description = rootView.findViewById(R.id.ic06_textView_description);
        image = rootView.findViewById(R.id.ic06_imageView);

        //image.setVisibility(hide);

        articleName.setText(this.headline.getTitle());
        author.setText(this.headline.getAuthor());
        date.setText(this.headline.getPublishedAt());
        description.setText(this.headline.getDescription());

        //image.setImageResource(headline.getUrlToImage());


        return rootView;
    }
}