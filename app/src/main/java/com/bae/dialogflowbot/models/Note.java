package com.bae.dialogflowbot.models;

public class Note {

    String title, content;
    String timestamp;

    public Note(String title, String content, String timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
