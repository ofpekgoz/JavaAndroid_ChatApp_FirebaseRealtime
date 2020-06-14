package com.omerfpekgoz.chatprojectfirebase.model;

public class User {

    private String userName;
    private String email;
    private String password;
    private String image;
    private String context;
    private String state;

    public User() {
    }

    public User(String userName, String email, String password, String image, String context, String state) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.image = image;
        this.context = context;
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getState() {
        return context;
    }

    public void setState(String state) {
        this.context = context;
    }
}
