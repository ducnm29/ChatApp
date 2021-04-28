package com.minhduc.chatapp;

import java.util.ArrayList;
import java.util.List;

public class ChatSession {
    private String idUser1,idUser2;
    private int count;
    private List<Message> messageList;

    public ChatSession() {
        messageList = new ArrayList<>();
    }

    public ChatSession(String idUser1, String idUser2, int count, List<Message> messageList) {
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.count = count;
        this.messageList = messageList;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public int getCount() {
        return count;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
}
