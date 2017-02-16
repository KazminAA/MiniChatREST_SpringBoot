package com.minichat.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.minichat.helpers.ViewProfiles;
import com.minichat.models.Message;
import com.minichat.models.User;
import com.minichat.repositories.MessageRepository;
import com.minichat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

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

    @RequestMapping(value = "/user/{login}", method = RequestMethod.GET, produces = "application/json")
    public User getUserByLogin(@PathVariable String login) {
        return userRepository.findUserByLogin(login);
    }

    @JsonView(ViewProfiles.MessageView.class)
    @RequestMapping(value = "/messages", method = RequestMethod.GET, produces = "application/json")
    public List<Message> getLast20Messages() {
        return messageRepository.findWithPageable(new PageRequest(0, 20, Sort.Direction.DESC, "id"));
    }

    @JsonView(ViewProfiles.MessageView.class)
    @RequestMapping(value = "/messages/{count}", method = RequestMethod.GET, produces = "application/json")
    public List<Message> getLastNMessages(@PathVariable int count) {
        return messageRepository.findWithPageable(new PageRequest(0, count, Sort.Direction.DESC, "id"));
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = "application/json")
    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST, consumes = "application/json")
    public void addMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.saveAndFlush(message);
    }
}
