package com.example.cs4520_inclassassignments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 06
 */
public class ArticleDisplay extends Fragment {

    TextView articleName, author, date, description;
    ImageView image;

    private static final String ARG_HEADLINE = "givenHeadline";

    private final Headline headline;

    public ArticleDisplay(Headline headline) {
        // Required empty public constructor
        this.headline = headline;
    }

    public static ArticleDisplay newInstance(Headline headline) {
        ArticleDisplay fragment = new ArticleDisplay(headline);
        Bundle args = new Bundle();
        args.putParcelable(ARG_HEADLINE, headline);
        fragment.setArguments(args);
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
        View rootView = inflater.inflate(R.layout.fragment_ic06_article_display, container, false);

        articleName = rootView.findViewById(R.id.ic06_textView_articleName);
        author = rootView.findViewById(R.id.ic06_textView_author);
        date = rootView.findViewById(R.id.ic06_textView_dateTime);
        description = rootView.findViewById(R.id.ic06_textView_description);
        image = rootView.findViewById(R.id.ic06_imageView);

        articleName.setText(this.headline.getTitle());
        author.setText(this.headline.getAuthor());
        date.setText(this.headline.getPublishedAt());
        description.setText(this.headline.getDescription());
        Picasso.get().load(this.headline.getUrlToImage()).into(image);

        return rootView;
    }
}