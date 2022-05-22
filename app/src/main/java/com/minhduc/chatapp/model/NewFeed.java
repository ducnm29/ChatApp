package com.minhduc.chatapp.model;

public class NewFeed {
    private String caption,imgLink,date;
    private User user;

    public NewFeed(String caption, String imgLink, String date, User user) {
        this.caption = caption;
        this.imgLink = imgLink;
        this.date = date;
        this.user = user;
    }

    public NewFeed() {
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
