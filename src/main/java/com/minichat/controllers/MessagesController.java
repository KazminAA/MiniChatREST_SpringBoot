package com.minichat.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.minichat.helpers.ViewProfiles;
import com.minichat.models.Message;
import com.minichat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/messages")
public class MessagesController {

    private MessageRepository messageRepository;

    @Autowired
    public MessagesController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @JsonView(ViewProfiles.MessageView.class)
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Message> getLast20Messages() {
        return messageRepository.findWithPageable(new PageRequest(0, 20, Sort.Direction.DESC, "id"));
    }

    @JsonView(ViewProfiles.MessageView.class)
    @RequestMapping(value = "{count}", method = RequestMethod.GET, produces = "application/json")
    public List<Message> getLastNMessages(@PathVariable int count) {
        return messageRepository.findWithPageable(new PageRequest(0, count, Sort.Direction.DESC, "id"));
    }

    @JsonView(ViewProfiles.MessageViewPost.class)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpHeaders addMessage(@RequestBody Message message, UriComponentsBuilder ucBuilder) {
        message.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        messageRepository.saveAndFlush(message);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ucBuilder.path("/messages").build().toUri());
        return httpHeaders;
    }
}
