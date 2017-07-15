package com.sivanta.chatapp;

/**
 * Created by chandan on 7/12/2017.
 */

public class ChatMessage1 {
    private String messageText;
    private UserType userType;
    private Status messageStatus;
    private long messageTime;

    public ChatMessage1(String messageText,)
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {

        this.messageText = messageText;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Status getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Status messageStatus) {
        this.messageStatus = messageStatus;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }


}
