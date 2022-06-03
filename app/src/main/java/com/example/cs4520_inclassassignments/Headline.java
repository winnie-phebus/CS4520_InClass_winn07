package com.example.cs4520_inclassassignments;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * TEAM 06
 *
 * @author Alix Heudebourg & Winnie Phebus
 * Assignment 06
 */
public class Headline implements Parcelable {

    private String title;
    private String author;
    private String publishedAt;
    private String description;
    private String urlToImage;

    public Headline(String title, String author, String publishedAt, String description, String urlToImage) {
        this.title = title;
        this.author = author;
        this.publishedAt = publishedAt;
        this.description = description;
        this.urlToImage = urlToImage;
    }

    protected Headline(Parcel in) {
        title = in.readString();
        author = in.readString();
        publishedAt = in.readString();
        description = in.readString();
        urlToImage = in.readString();
    }

    public static final Creator<Headline> CREATOR = new Creator<Headline>() {
        @Override
        public Headline createFromParcel(Parcel in) {
            return new Headline(in);
        }

        @Override
        public Headline[] newArray(int size) {
            return new Headline[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    @Override
    public String toString() {
        return "Headline{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", description='" + description + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getTitle());
        dest.writeString(getAuthor());
        dest.writeString(getPublishedAt());
        dest.writeString(getDescription());
        dest.writeString(getUrlToImage());
    }
}


