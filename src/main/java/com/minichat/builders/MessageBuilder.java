package com.minichat.builders;

import com.minichat.models.Message;
import com.minichat.models.User;

import java.time.LocalDateTime;

public class MessageBuilder {
    private long id = 1;
    private String messageText = "Test message";
    private User user = new UserBuilder().build();
    private LocalDateTime timestamp = LocalDateTime.of(1900, 1, 1, 1, 1);

    public MessageBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public MessageBuilder withMessageText(String messageText) {
        this.messageText = messageText;
        return this;
    }

    public MessageBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public MessageBuilder with1tUser() {
        user = new UserBuilder().build();
        return this;
    }

    public MessageBuilder with2dUser() {
        user = new UserBuilder().withId(2).withLogin("login2").withName("name2")
                .withMail("mail2@mail.ru").build();
        return this;
    }

    public MessageBuilder withTime(LocalDateTime time) {
        timestamp = time;
        return this;
    }

    public Message build() {
        Message message = new Message(messageText, user, timestamp);
        message.setId(id);
        return message;
    }
}
