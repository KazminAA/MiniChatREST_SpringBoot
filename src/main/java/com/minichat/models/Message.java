package com.minichat.models;

import java.time.LocalDateTime;

/**
 * Created by lex on 23.01.17.
 */
public class Message {
    private long id;
    private String messageText;
    private User user;
    private LocalDateTime timestamp;

    public Message() {
    }

    public Message(String messageText, User user, LocalDateTime timestamp) {
        this.messageText = messageText;
        this.user = user;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (getId() != message.getId()) return false;
        return getTimestamp() != null ? getTimestamp().equals(message.getTimestamp()) : message.getTimestamp() == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" + user +
                ": (" + timestamp + ") '" +
                messageText + "'" +
                '}';
    }
}
