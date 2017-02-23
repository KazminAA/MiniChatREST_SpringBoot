package com.minichat.controllers;

import com.minichat.models.User;
import com.minichat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "{login}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getUserByLogin(@PathVariable String login) {
        User user = userRepository.findUserByLogin(login);
        if (user == null) {
            String error = String.format("User with login %s not found", login);
            return new ResponseEntity(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user, Errors errors, UriComponentsBuilder ucBuilder) {
        if (errors.hasErrors()) {
            return new ResponseEntity<Object>(errors.toString(), HttpStatus.BAD_REQUEST);
        }
        userRepository.saveAndFlush(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ucBuilder.path("/users/{login}").buildAndExpand(user.getLogin()).toUri());
        return new ResponseEntity<String>(httpHeaders, HttpStatus.CREATED);
    }
}
