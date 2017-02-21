package com.minichat.controllers;

import com.minichat.models.User;
import com.minichat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "{login}", method = RequestMethod.GET, produces = "application/json")
    public User getUserByLogin(@PathVariable String login) {
        return userRepository.findUserByLogin(login);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public User addUser(@RequestBody @Valid User user, Errors errors) {
        if (!errors.hasErrors()) {
            return userRepository.saveAndFlush(user);
        }
        return user;
    }
}
