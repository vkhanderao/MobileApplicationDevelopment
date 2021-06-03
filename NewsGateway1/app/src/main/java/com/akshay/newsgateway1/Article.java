package com.akshay.newsgateway1;

import java.io.Serializable;

public class Article implements Serializable {

    private String authorName;
    private String newsTitle;
    private String newsDescription;
    private String newsUrl;
    private String imageUrl;
    private String publishedDate;

    public Article(String authorName, String newsTitle, String newsDescription, String newsUrl, String imageUrl, String publishedDate) {
        this.authorName = authorName;
        this.newsTitle = newsTitle;
        this.newsDescription = newsDescription;
        this.newsUrl = newsUrl;
        this.imageUrl = imageUrl;
        this.publishedDate = publishedDate;
    }

    public String getAuthor() {
        return authorName;
    }

    public void setAuthor(String author) {
        this.authorName = author;
    }

    public String getTitle() {
        return newsTitle;
    }

    public void setTitle(String title) {
        this.newsTitle = removeRegex(title);
    }

    public String getDescription() {
        return newsDescription;
    }

    public void setDescription(String description) {
        this.newsDescription = removeRegex(description);
    }

    public String getUrl() {
        return newsUrl;
    }

    public void setUrl(String url) {
        this.newsUrl = url;
    }

    public String getUrltoImage() {
        return imageUrl;
    }

    public void setUrltoImage(String urltoImage) {
        this.imageUrl = urltoImage;
    }

    public String getPublishedAt() {
        return publishedDate;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedDate = publishedAt;
    }

    private String removeRegex(String s)
    {
        return s.replaceAll("\\<(/?[^\\>]+)\\>", "\\ ").replaceAll("\\s+", " ").trim();
    }
}