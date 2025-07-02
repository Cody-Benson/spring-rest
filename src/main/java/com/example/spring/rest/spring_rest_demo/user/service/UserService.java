package com.example.spring.rest.spring_rest_demo.user.service;

import org.springframework.stereotype.Service;
import java.util.Optional;
import com.example.spring.rest.spring_rest_demo.user.model.User;

@Service
public class UserService {
    
    public Optional<User> getUserByFirstName(String userName){
        Optional<User> newUser = Optional.of(new User(userName));
        return newUser;
    }
}
