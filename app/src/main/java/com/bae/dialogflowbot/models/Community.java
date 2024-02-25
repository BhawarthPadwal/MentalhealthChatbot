package com.bae.dialogflowbot.models;

import java.util.Map;

public class Community {

    String name, imageUrl, message;
    Map<String, Object> timestamp;

    public Community() {
    }
    public Community(String name, String imageUrl, String message, Map<String, Object> timestamp) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map<String, Object> timestamp) {
        this.timestamp = timestamp;
    }
}
