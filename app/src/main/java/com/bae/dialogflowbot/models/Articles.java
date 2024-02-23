package com.bae.dialogflowbot.models;

public class Articles {

    String title, imageUrl, content;

    public Articles() {
    }

    public Articles(String title, String imageUrl, String content) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getContent() {
        return content;
    }
}
