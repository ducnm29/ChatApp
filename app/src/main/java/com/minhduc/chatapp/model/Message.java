package com.minhduc.chatapp.model;

public class Message {
    private String idSender,idReceiver,message,time,imageUrl;

    public Message(String idSender, String idReceiver, String message, String time, String imageUrl) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    public Message() {
    }

    public String getIdSender() {
        return idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
