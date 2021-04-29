package com.minhduc.chatapp;

public class User {
    private String id,username,imageurl,password,email,status;

    public User(String id, String imageurl,String password, String username,String email,String status) {
        this.id = id;
        this.imageurl = imageurl;
        this.password = password;
        this.username = username;
        this.email = email;
        this.status=status;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public  String getPassword(){
        return password;
    }
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageurl;
    }
    public String getStatus(){
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageurl = imageUrl;
    }
    public void setStatus(String status){
        this.status = status;
    }
}
