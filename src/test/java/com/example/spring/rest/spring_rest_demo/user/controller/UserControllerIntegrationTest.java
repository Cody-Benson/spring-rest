package com.example.spring.rest.spring_rest_demo.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.example.spring.rest.spring_rest_demo.SpringRestDemoApplication;
import com.example.spring.rest.spring_rest_demo.user.dto.UserCreateDTO;
import com.example.spring.rest.spring_rest_demo.user.dto.UserReplaceDTO;
import com.example.spring.rest.spring_rest_demo.user.model.User;
import com.example.spring.rest.spring_rest_demo.user.repository.UserRepository;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = SpringRestDemoApplication.class)
@TestPropertySource(
  locations = "classpath:application-test.properties")
public class UserControllerIntegrationTest {
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
    }

    @Test
    void userController_GetUsers_ReturnsAListOfUsers_IT(){
      User john = User.builder()
      .firstName("john")
      .lastName("doe")
      .email("johndoe@mail.com").build();
      userRepository.save(john);

      User cody = User.builder()
      .firstName("cody")
      .lastName("benson")
      .email("codybenson@mail.com").build();
      userRepository.save(cody);

      ResponseEntity<List<User>> response = testRestTemplate.exchange(
            "/user",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<User>>() {}
        );
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(2, response.getBody().size());
    }

    @Test
    void userController_GetUserById_ReturnsAUser(){
       User john = User.builder()
      .firstName("john")
      .lastName("doe")
      .email("johndoe@mail.com").build();
      User savedUser = userRepository.save(john);

      ResponseEntity<User> response = testRestTemplate.exchange(
            "/user/" + savedUser.getId(),
            HttpMethod.GET,
            null,
            User.class
        );
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(savedUser.getId(),response.getBody().getId());
    }

    @Test
    void userController_GetUserById_ReturnsResourceNotFoundException(){

      ResponseEntity<User> response = testRestTemplate.exchange(
            "/user/1",
            HttpMethod.GET,
            null,
            User.class
        );

      assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void userController_DeleteUserById_UserIsDeletedFromDatabaseAndNoContentStatusIsReturned(){
      User john = User.builder()
      .firstName("john")
      .lastName("doe")
      .email("johndoe@mail.com").build();
      User savedUser = userRepository.save(john);

      ResponseEntity<Void> response = testRestTemplate.exchange(
        "/user/"+ savedUser.getId(),
        HttpMethod.DELETE,
        null,
        Void.class
        );

      assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
      int userCountInDB = 0;
      assertEquals(userCountInDB,userRepository.findAll().size());
    }

    @Test
    void userController_CreateUser_CreatesUserInDBAndReturnsUser(){
      UserCreateDTO userCreateDTO = UserCreateDTO.builder()
      .firstName("cody")
      .lastName("benson")
      .email("cody@mail.com")
      .build();
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<UserCreateDTO> httpEntity = new HttpEntity<>(userCreateDTO, headers);

      ResponseEntity<User> response = testRestTemplate.exchange(
            "/user",
            HttpMethod.POST,
            httpEntity,
            User.class
        );

      assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
      
      List<User> users = userRepository.findAll();
      User user = users.get(0);
      assertEquals(response.getBody().getId(), user.getId());
    }

    @Test
    void userController_CreateUser_ReturnsConflictException(){
      UserCreateDTO userCreateDTO = UserCreateDTO.builder()
      .firstName("cody")
      .lastName("benson")
      .email("cody@mail.com")
      .build();
      
      User user = User.builder()
      .firstName("cody")
      .lastName("benson")
      .email("cody@mail.com")
      .build();

      userRepository.save(user);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<UserCreateDTO> httpEntity = new HttpEntity<>(userCreateDTO, headers);

      ResponseEntity<User> response = testRestTemplate.exchange(
            "/user",
            HttpMethod.POST,
            httpEntity,
            User.class
        );

      assertEquals(HttpStatusCode.valueOf(409), response.getStatusCode());
    }

    @Test
    void userController_ReplaceUser_ShouldUpdateUserInDB(){
      User john = User.builder()
      .firstName("john")
      .lastName("doe")
      .email("johndoe@mail.com").build();
      User savedUser = userRepository.save(john);
      
      UserReplaceDTO replaceDTO = UserReplaceDTO.builder()
      .firstName("cody")
      .lastName("benson")
      .email("cody@mail.com").build();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<UserReplaceDTO> httpEntity = new HttpEntity<>(replaceDTO,headers);
      ResponseEntity<User> response = testRestTemplate.exchange(
        "/user/" + savedUser.getId(),
        HttpMethod.PUT,
        httpEntity,
        User.class
      );

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals("cody",response.getBody().getFirstName());
      
      Long updatedUserId = savedUser.getId();
      User updatedUser = userRepository.getReferenceById(updatedUserId);
      assertEquals(response.getBody().getId(),updatedUser.getId());
    }
}