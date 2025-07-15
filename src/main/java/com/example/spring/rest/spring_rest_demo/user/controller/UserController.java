package com.example.spring.rest.spring_rest_demo.user.controller;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring.rest.spring_rest_demo.api_response.APIResponse;
import com.example.spring.rest.spring_rest_demo.user.dto.UserCreateDTO;
import com.example.spring.rest.spring_rest_demo.user.model.User;
import com.example.spring.rest.spring_rest_demo.user.service.UserService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/:id")
    public ResponseEntity<User> getUserById(Long id){
        User user = userService.getUserById(id);
    }

}