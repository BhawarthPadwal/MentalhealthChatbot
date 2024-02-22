package com.bae.dialogflowbot.models;

public class RelaxingSounds {

    String name, duration, url;

    public RelaxingSounds() {
    }

    public RelaxingSounds(String name, String duration, String url) {
        this.name = name;
        this.duration = duration;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }
}
