package com.example.spring.rest.spring_rest_demo.user.controller;
import org.springframework.web.bind.annotation.RestController;
import com.example.spring.rest.spring_rest_demo.user.model.User;
import com.example.spring.rest.spring_rest_demo.user.service.UserService;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    public User getUser(){
        Optional<User> user = userService.getUserByFirstName("cody");
        return user.get();
    }
}