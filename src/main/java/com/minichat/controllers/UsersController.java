package com.minichat.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.minichat.helpers.ViewProfiles;
import com.minichat.models.User;
import com.minichat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UsersController {

    private UserRepository userRepository;

    @Autowired
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @JsonView(ViewProfiles.UserView.class)
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @JsonView(ViewProfiles.UserView.class)
    @RequestMapping(value = "{login}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getUserByLogin(@PathVariable String login) {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            String error = String.format("User with login %s not found", login);
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @JsonView(ViewProfiles.UserPost.class)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public HttpHeaders addUser(@RequestBody @Valid User user, UriComponentsBuilder ucBuilder) {
        userRepository.saveAndFlush(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ucBuilder.path("/users/{login}").buildAndExpand(user.getLogin()).toUri());
        return httpHeaders;
    }
}
