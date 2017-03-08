package com.minichat.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.minichat.helpers.ViewProfiles;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @Column(name = "messtext")
    @JsonView(ViewProfiles.MessageView.class)
    private String messageText;
    @ManyToOne
    @JoinColumn(name = "user_ID", referencedColumnName = "ID")
    @JsonView(ViewProfiles.MessageView.class)
    private User user;
    @Column(name = "timestamp")
    @JsonView(ViewProfiles.MessageView.class)
    private Timestamp timestamp;

    public Message() {
    }

    public Message(String messageText, User user, Timestamp timestamp) {
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = (timestamp != null) ? Timestamp.valueOf(timestamp) : null;
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
