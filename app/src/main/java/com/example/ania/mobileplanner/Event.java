package com.example.ania.mobileplanner;

public class Event {

    private String title;
    private String description;
    private String date;
    private String time;
    private String notification;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Event(String title, String description, String date, String time, String notification) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.notification = notification;
    }

    public Event(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
