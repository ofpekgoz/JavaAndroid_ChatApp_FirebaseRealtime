package com.omerfpekgoz.chatprojectfirebase.model;

public class Message {


    private String date;
    private String from;
    private String messageText;
    private String messageType;
    private boolean seen;

    public Message() {
    }

    public Message(String date, String from, String messageText, String messageType, boolean seen) {
        this.date = date;
        this.from = from;
        this.messageText = messageText;
        this.messageType = messageType;
        this.seen = seen;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}

