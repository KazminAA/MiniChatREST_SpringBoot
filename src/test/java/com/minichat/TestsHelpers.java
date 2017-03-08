package com.minichat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minichat.builders.MessageBuilder;
import com.minichat.builders.UserBuilder;
import com.minichat.models.Message;
import com.minichat.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestsHelpers {
    private static UserBuilder userBuilder = new UserBuilder();
    private static MessageBuilder messageBuilder = new MessageBuilder();

    public static byte[] getObjectAsJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static List<User> createUsersList(int count) {
        List<User> users = new ArrayList<>();
        User user;
        for (int i = 0; i < count; i++) {
            user = userBuilder
                    .withId(i)
                    .withLogin("login" + i)
                    .withName("name" + i)
                    .withMail("mail" + i + "@mail.ru")
                    .build();
            user.setId(i);
            users.add(user);
        }
        return users;
    }

    public static List<Message> createMessagesList(int count) {
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            messages.add(messageBuilder.withId(i + 1).with1tUser().withMessageText("Message " + (i + 1)).build());
        }
        return messages;
    }
}
