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
import com.example.spring.rest.spring_rest_demo.user.dto.UserReplaceDTO;
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
        User user1 = User.builder()
        .id(1l)
        .build();

        User user2 = User.builder()
        .id(2l)
        .build();

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> response = userService.getUsers();

        User result1 = response.get(0);
        User result2 = response.get(1);
        Assertions.assertEquals(result1.getId(), user1.getId());
        Assertions.assertEquals(result2.getId(), user2.getId());
    }

    @Test
    public void UserService_GetUserById_ReturnsUserWithId(){
        User user = User.builder()
        .id(1l)
        .build();

        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        User result = userService.getUserById(1l);
        Assertions.assertEquals(user.getId(), result.getId());
    }

    @Test
    public void UserService_GetUserById_ThrowsResourceNotFoundException(){
        when(userRepository.findById(1l)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1l));
        verify(userRepository, times(1)).findById(1l);
    }

    @Test
    public void UserService_deleteUserById_VerifiesRepositoryWasCalled(){
        User user = User.builder().id(1l).build();
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
        UserCreateDTO userCreateDTO = UserCreateDTO.builder().build();
        when(userRepository.existsByEmail("cody@mail.com")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.createUser(userCreateDTO));
    }

    @Test
    public void UserService_createUser_ReturnsCreatedUser(){
        UserCreateDTO userCreateDTO = UserCreateDTO.builder().build();
        User user = User.builder().id(1l).build();

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

    @Test
    public void UserService_replaceUser_ReturnsUpdatedUser(){
        UserReplaceDTO userReplaceDTO = UserReplaceDTO.builder().build();
        User user = User.builder().build();

        when(userRepository.findById(1l)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.replaceUser(1l, userReplaceDTO);

        //capture the user
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        //test the object mapping of userDTO to user
        assertEquals(userReplaceDTO.getFirstName(), capturedUser.getFirstName());
        assertEquals(userReplaceDTO.getLastName(), capturedUser.getLastName());
        assertEquals(userReplaceDTO.getEmail(), capturedUser.getEmail());
        assertEquals(userReplaceDTO.getPassword(), capturedUser.getPassword());
        assertEquals(userReplaceDTO.getPhoneNumber(), capturedUser.getPhoneNumber());

        //test the saved user is returned
        assertEquals(result.getFirstName(), user.getFirstName());
        assertEquals(result.getLastName(), user.getLastName());
        assertEquals(result.getEmail(), user.getEmail());
        assertEquals(result.getPassword(), user.getPassword());
        assertEquals(result.getPhoneNumber(), user.getPhoneNumber());
    }
}
