package com.example.cs4520_inclassassignments.inClass06;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs4520_inclassassignments.R;

import java.util.List;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 06
 */
public class HeadlineAdapter extends RecyclerView.Adapter<HeadlineAdapter.ViewHolder> {
    List<Headline> articles;
    FragmentActivity fContext;

    public HeadlineAdapter(List<Headline> articles, FragmentActivity context) {
        if (context != null) {
            this.articles = articles;
            this.fContext = context;
        } else {
            throw new RuntimeException(context.toString() + "must be called from Fragment");
        }
    }

    public List<Headline> getArticles() {
        return articles;
    }

    public void setArticles(List<Headline> articles) {
        this.articles = articles;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout container;
        private TextView titleTV;
        private TextView authorTV;
        private TextView publishedAtTV;
        private TextView descriptionTV;
        // private TextView urlToImageTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.container = itemView.findViewById(R.id.ic06_headlineV);
            this.titleTV = itemView.findViewById(R.id.ic06_hTitle);
            this.authorTV = itemView.findViewById(R.id.ic06_hAuthor);
            this.publishedAtTV = itemView.findViewById(R.id.ico06_hDate);
            this.descriptionTV = itemView.findViewById(R.id.ic06_hDescription);
            // this.urlToImageTV = itemView.findViewById();
        }

        public ConstraintLayout getContainer() {
            return container;
        }

        public TextView getTitleTV() {
            return titleTV;
        }

        public TextView getAuthorTV() {
            return authorTV;
        }

        public TextView getPublishedAtTV() {
            return publishedAtTV;
        }

        public TextView getDescriptionTV() {
            return descriptionTV;
        }

        /*public TextView getUrlToImageTV() {
            return urlToImageTV;
        }*/
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.ic06_headline_view, parent, false);

        return new ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Headline curr = this.getArticles().get(position);

        holder.getTitleTV().setText(curr.getTitle());

        String author = curr.getAuthor();
        if (TextUtils.isEmpty(author)) {
            holder.getAuthorTV().setVisibility(View.GONE);
        } else {
            holder.getAuthorTV().setText(curr.getAuthor());
        }

        String date = curr.getPublishedAt();
        if (TextUtils.isEmpty(date)) {
            holder.getPublishedAtTV().setVisibility(View.GONE);
        } else {
            holder.getPublishedAtTV().setText(date);
        }
        String description = curr.getDescription();
        if (TextUtils.isEmpty(description)) {
            holder.getDescriptionTV().setVisibility(View.GONE);
        } else {
            holder.getDescriptionTV().setText(curr.getDescription());
        }

        holder.getContainer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fContext instanceof InClass06) {
                    ((InClass06) fContext).openArticle(curr);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getArticles().size();
    }
}
