package com.bae.dialogflowbot.models;

import java.util.Map;

public class Note {
    String title, content;
    Map<String, Object> timestamp;
    public Note(String title, String content, Map<String, Object> timestamp) {
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

    public Map<String, Object> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map<String, Object> timestamp) {
        this.timestamp = timestamp;
    }
}
