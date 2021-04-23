package com.example.medimate;

public class Model {


    String title, time, description, source, url, urlToImage;

    public Model(String title, String time, String description, String source, String url, String urlToImage) {

        this.title = title;
        this.time = time;
        this.description = description;
        this.source = source;
        this.url = url;
        this.urlToImage = urlToImage;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }
}