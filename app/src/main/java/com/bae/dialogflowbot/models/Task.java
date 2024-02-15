package com.bae.dialogflowbot.models;

import java.util.Map;

public class Task {

    String title, description;
    Map<String, Integer> date;
    int status;

    public Task() {
    }

    public Task(String title, String description, Map<String, Integer> date, int status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }


    public Map<String, Integer> getDate() {
        return date;
    }

    public void setDate(Map<String, Integer> date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
