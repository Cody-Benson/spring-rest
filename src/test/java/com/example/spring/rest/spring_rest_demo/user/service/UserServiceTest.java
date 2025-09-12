package com.example.spring.rest.spring_rest_demo.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.spring.rest.spring_rest_demo.user.dto.UserCreateDTO;
import com.example.spring.rest.spring_rest_demo.user.exception.ConflictException;
import com.example.spring.rest.spring_rest_demo.user.exception.ResourceNotFoundException;
import com.example.spring.rest.spring_rest_demo.user.model.User;
import com.example.spring.rest.spring_rest_demo.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void UserService_GetUsers_ReturnsListOfUsers(){
        User user = new User();
        user.setId(1l);
        user.setFirstName("Cody");
        user.setLastName("Benson");
        user.setEmail("codyobenson@gmail.com");
        user.setPassword("password");
        user.setPhoneNumber(1234567890);

        User user2 = new User();
        user.setId(2l);
        user.setFirstName("John");
        user.setLastName("Benson");
        user.setEmail("johnobenson@gmail.com");
        user.setPassword("password");
        user.setPhoneNumber(1234567890);

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> response = userService.getUsers();
        Assertions.assertEquals(users, response);
    }

    @Test
    public void UserService_GetUserById_ReturnsUserWithId(){
        User user = new User();
        user.setId(1l);
        user.setFirstName("Cody");
        user.setLastName("Benson");
        user.setEmail("codyobenson@gmail.com");
        user.setPassword("password");
        user.setPhoneNumber(1234567890);
        
        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        
        User result = userService.getUserById(1l);
        
        Assertions.assertEquals(user.getId(), result.getId());
    }

    @Test
    public void UserService_GetUserById_ThrowsResourceNotFoundException(){
        User user = new User();
        user.setId(2l);
        user.setFirstName("Cody");
        user.setLastName("Benson");
        user.setEmail("codyobenson@gmail.com");
        user.setPassword("password");
        user.setPhoneNumber(1234567890);
        
        when(userRepository.findById(1l)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1l));
        verify(userRepository, times(1)).findById(1l);
    }

    @Test
    public void UserService_deleteUserById_VerifiesRepositoryWasCalled(){
        User user = new User();
        user.setId(1l);
        user.setFirstName("Cody");
        user.setLastName("Benson");
        user.setEmail("codyobenson@gmail.com");
        user.setPassword("password");
        user.setPhoneNumber(1234567890);

        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUserById(1l);

        verify(userRepository, times(1)).findById(1l);
        verify(userRepository, times(1)).deleteById(1l);
    }

    @Test
    public void UserService_userEmailExists_ReturnsTrueIfEmailFound(){
        when(userRepository.existsByEmail("cody@mail.com")).thenReturn(true);
        boolean emailExists = userService.userEmailExists("cody@mail.com");
        assertTrue(emailExists);
    }

    @Test
    public void UserService_userEmailExists_ReturnsFalseIfEmailNotFound(){
        when(userRepository.existsByEmail("cody@mail.com")).thenReturn(false);
        boolean emailExists = userService.userEmailExists("cody@mail.com");
        assertFalse(emailExists);
    }

    @Test
    public void UserService_createUser_ThrowsConflictException(){
        UserCreateDTO userCreateDTO = new UserCreateDTO("cody", "benson", "cody@mail.com", 1234567890, "password");
        
        when(userRepository.existsByEmail("cody@mail.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.createUser(userCreateDTO));
    }

    @Test
    public void UserService_createUser_ReturnsCreatedUser(){
        UserCreateDTO userCreateDTO = new UserCreateDTO("cody", "benson", "cody@mail.com", 1234567890, "password");
        User user = new User();
        user.setId(1l);
        user.setFirstName("cody");
        user.setLastName("benson");
        user.setEmail("cody@mail.com");
        user.setPassword("password");
        user.setPhoneNumber(1234567890);

        when(userRepository.existsByEmail("cody@mail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        User result = userService.createUser(userCreateDTO);
        
        //capture the user
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        
        //test the object mapping of userDTO to user
        assertEquals(userCreateDTO.getFirstName(), capturedUser.getFirstName());
        assertEquals(userCreateDTO.getLastName(), capturedUser.getLastName());
        assertEquals(userCreateDTO.getEmail(), capturedUser.getEmail());
        assertEquals(userCreateDTO.getPassword(), capturedUser.getPassword());
        assertEquals(userCreateDTO.getPhoneNumber(), capturedUser.getPhoneNumber());

        //test the saved user is returned
        assertEquals(result.getFirstName(), user.getFirstName());
        assertEquals(result.getLastName(), user.getLastName());
        assertEquals(result.getEmail(), user.getEmail());
        assertEquals(result.getPassword(), user.getPassword());
        assertEquals(result.getPhoneNumber(), user.getPhoneNumber());
    }
}
