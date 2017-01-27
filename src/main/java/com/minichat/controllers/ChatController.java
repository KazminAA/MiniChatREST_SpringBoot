package com.minichat.controllers;

import com.minichat.models.User;
import com.minichat.repositories.MessageRepository;
import com.minichat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/chat")
public class ChatController {
    private UserRepository userRepository;
    private MessageRepository messageRepository;

    public ChatController() {
    }

    @Autowired
    public ChatController(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = "application/json")
    public User getUserById(@PathVariable long id) {
        return userRepository.findOne(id);
    }
}
