package com.example.spring.rest.spring_rest_demo.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.example.spring.rest.spring_rest_demo.user.dto.UserCreateDTO;
import com.example.spring.rest.spring_rest_demo.user.model.User;
import com.example.spring.rest.spring_rest_demo.user.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            
        }
        return user;
    }

    public boolean deleteUserById(Long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    public boolean userEmailExists(String email){
        boolean emailExists = userRepository.existsByEmail(email);
        return emailExists;
    }

    public User createUser(UserCreateDTO userDTO){
        User newUser = new User();
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setPassword(userDTO.getPassword());
        newUser = userRepository.save(newUser);
        
        return newUser;
    }
}