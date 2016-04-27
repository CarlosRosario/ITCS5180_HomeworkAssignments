package com.example.group26.stayintouchwithfragments;

/**
 * Created by Carlos on 4/17/2016.
 */
public class Message {

    private String timeStamp;
    private boolean message_read = false;
    private String message_text;
    private String receiver;
    private String sender;
    private String deletedBy;

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isMessage_read() {
        return message_read;
    }

    public void setMessage_read(boolean message_read) {
        this.message_read = message_read;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}