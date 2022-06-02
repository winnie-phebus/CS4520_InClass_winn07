package com.example.cs4520_inclassassignments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HeadlineAdapter extends RecyclerView.Adapter<HeadlineAdapter.ViewHolder> {
    List<Headline> articles;

    public HeadlineAdapter(List<Headline> articles, Context context) {
        this.articles = articles;
    }

    public List<Headline> getArticles() {
        return articles;
    }

    public void setArticles(List<Headline> articles) {
        this.articles = articles;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTV;
        private TextView authorTV;
        private TextView publishedAtTV;
        private TextView descriptionTV;
       // private TextView urlToImageTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.titleTV = itemView.findViewById(R.id.ic06_hTitle);
            this.authorTV = itemView.findViewById(R.id.ic06_hAuthor);
            this.publishedAtTV = itemView.findViewById(R.id.ico06_hDate);
            this.descriptionTV = itemView.findViewById(R.id.ic06_hDescription);
           // this.urlToImageTV = itemView.findViewById();
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
                .inflate(R.layout.ic06_headline_view,parent, false);

        return new ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Headline curr = this.getArticles().get(position);

        holder.getTitleTV().setText(curr.getTitle());
        holder.getAuthorTV().setText(curr.getAuthor());
        holder.getPublishedAtTV().setText(curr.getPublishedAt());
        holder.getDescriptionTV().setText(curr.getDescription());
        // TODO: figure out what you're doing with the image
    }

    @Override
    public int getItemCount() {
        if (getArticles() == null){
            return 0;
        }
        return getArticles().size();
    }
}
