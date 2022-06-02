package com.example.cs4520_inclassassignments;


import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Headline {

    private String title;
    private String author;
    private String publishedAt;
    private String description;
    private String urlToImage;

    /*public Headline(){
    }*/

    public Headline(String title, String author, String publishedAt, String description, String urlToImage) {
        this.title = title;
        this.author = author;
        this.publishedAt = publishedAt;
        this.description = description;
        this.urlToImage = urlToImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
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

    public String readbleDate(){
        // get readble date from UTC format
        // 2022-05-31T18:28:56Z -> May 31st, 2022 at 6:28

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
                .ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'");

        //Instance with given offset
        OffsetDateTime odtInstanceAtOffset = OffsetDateTime.parse(this.publishedAt,
                DATE_TIME_FORMATTER);

        String dateStringInUTC = odtInstanceAtOffset.format(DATE_TIME_FORMATTER);

        return dateStringInUTC;
    }
}


