package com.example.spring.rest.spring_rest_demo.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.spring.rest.spring_rest_demo.user.dto.UserCreateDTO;
import com.example.spring.rest.spring_rest_demo.user.dto.UserReplaceDTO;
import com.example.spring.rest.spring_rest_demo.user.exception.ConflictException;
import com.example.spring.rest.spring_rest_demo.user.exception.ResourceNotFoundException;
import com.example.spring.rest.spring_rest_demo.user.model.User;
import com.example.spring.rest.spring_rest_demo.user.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()){
            throw new ResourceNotFoundException("user with id:" + id + " does not exist.");
        }
        return user.get();
    }

    public void deleteUserById(Long id){
        getUserById(id);
        userRepository.deleteById(id);
        return;
    }

    public boolean userEmailExists(String email){
        boolean emailExists = userRepository.existsByEmail(email);
        return emailExists;
    }

    public User createUser(UserCreateDTO userDTO){
        String email = userDTO.getEmail();
        boolean emailExists = userRepository.existsByEmail(email);
        if(emailExists){
            throw new ConflictException("user with email:" + email + " already exists");
        }

        User newUser = User.builder()
        .firstName(userDTO.getFirstName())
        .lastName(userDTO.getLastName())
        .email(userDTO.getEmail())
        .phoneNumber(userDTO.getPhoneNumber())
        .password(userDTO.getPassword())
        .build();
        newUser = userRepository.save(newUser);

        return newUser;
    }

    public User replaceUser(Long id, UserReplaceDTO userDTO){
        User user = getUserById(id);

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());

        userRepository.save(user);
        return user;
    }
}