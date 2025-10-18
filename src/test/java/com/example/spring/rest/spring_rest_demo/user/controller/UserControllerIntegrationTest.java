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
  private final String USER1_FIRST_NAME = "john";
  private final String USER1_LAST_NAME = "doe";
  private final String USER1_EMAIL = "johndoe@mail.com";

  private final String USER2_FIRST_NAME = "cody";
  private final String USER2_LAST_NAME = "benson";
  private final String USER2_EMAIL = "codybenson@mail.com";

  private final String USER_ENDPOINT = "/user";
  private final String FORWARD_SLASH = "/";
  
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
      .firstName(USER1_FIRST_NAME)
      .lastName(USER1_LAST_NAME)
      .email(USER1_EMAIL).build();
      userRepository.save(john);

      User cody = User.builder()
      .firstName(USER2_FIRST_NAME)
      .lastName(USER2_LAST_NAME)
      .email(USER2_EMAIL).build();
      userRepository.save(cody);

      ResponseEntity<List<User>> response = testRestTemplate.exchange(
            USER_ENDPOINT,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<User>>() {}
        );
      
      assertEquals(HttpStatus.OK, response.getStatusCode());
      
      final int COUNT_OF_USERS = 2;
      assertEquals(COUNT_OF_USERS, response.getBody().size());
    }

    @Test
    void userController_GetUserById_ReturnsAUser_IT(){
       User john = User.builder()
      .firstName(USER1_FIRST_NAME)
      .lastName(USER1_LAST_NAME)
      .email(USER1_EMAIL).build();
      User savedUser = userRepository.save(john);

      ResponseEntity<User> response = testRestTemplate.exchange(
            USER_ENDPOINT + FORWARD_SLASH + savedUser.getId(),
            HttpMethod.GET,
            null,
            User.class
        );
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(savedUser.getId(),response.getBody().getId());
    }

    @Test
    void userController_GetUserById_ReturnsResourceNotFoundException(){
      final String USER_1 = "1";

      ResponseEntity<User> response = testRestTemplate.exchange(
            USER_ENDPOINT + FORWARD_SLASH + USER_1,
            HttpMethod.GET,
            null,
            User.class
        );

      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void userController_DeleteUserById_UserIsDeletedFromDatabaseAndNoContentStatusIsReturned(){
      final int USER_COUNT_IN_DB = 0;
      User john = User.builder()
      .firstName(USER1_FIRST_NAME)
      .lastName(USER1_LAST_NAME)
      .email(USER1_EMAIL).build();
      User savedUser = userRepository.save(john);

      ResponseEntity<Void> response = testRestTemplate.exchange(
        USER_ENDPOINT + FORWARD_SLASH + savedUser.getId(),
        HttpMethod.DELETE,
        null,
        Void.class
        );

      assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
      assertEquals(USER_COUNT_IN_DB,userRepository.findAll().size());
    }

    @Test
    void userController_CreateUser_CreatesUserInDBAndReturnsUser(){
      UserCreateDTO userCreateDTO = UserCreateDTO.builder()
      .firstName(USER2_FIRST_NAME)
      .lastName(USER2_LAST_NAME)
      .email(USER2_EMAIL)
      .build();
      
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<UserCreateDTO> httpEntity = new HttpEntity<>(userCreateDTO, headers);

      ResponseEntity<User> response = testRestTemplate.exchange(
            USER_ENDPOINT,
            HttpMethod.POST,
            httpEntity,
            User.class
        );

      assertEquals(HttpStatus.CREATED, response.getStatusCode());
      
      List<User> users = userRepository.findAll();
      User user = users.get(0);
      assertEquals(response.getBody().getId(), user.getId());
    }

    @Test
    void userController_CreateUser_ReturnsConflictException(){
      UserCreateDTO userCreateDTO = UserCreateDTO.builder()
      .firstName(USER2_FIRST_NAME)
      .lastName(USER2_LAST_NAME)
      .email(USER2_EMAIL)
      .build();
      
      User user = User.builder()
      .firstName(USER2_FIRST_NAME)
      .lastName(USER2_LAST_NAME)
      .email(USER2_EMAIL)
      .build();

      userRepository.save(user);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<UserCreateDTO> httpEntity = new HttpEntity<>(userCreateDTO, headers);

      ResponseEntity<User> response = testRestTemplate.exchange(
            USER_ENDPOINT,
            HttpMethod.POST,
            httpEntity,
            User.class
        );

      assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void userController_ReplaceUser_ShouldUpdateUserInDB(){
      User john = User.builder()
      .firstName(USER1_FIRST_NAME)
      .lastName(USER1_LAST_NAME)
      .email(USER1_EMAIL).build();
      User savedUser = userRepository.save(john);
      
      UserReplaceDTO replaceDTO = UserReplaceDTO.builder()
      .firstName(USER2_FIRST_NAME)
      .lastName(USER2_LAST_NAME)
      .email(USER2_EMAIL).build();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<UserReplaceDTO> httpEntity = new HttpEntity<>(replaceDTO,headers);
      ResponseEntity<User> response = testRestTemplate.exchange(
        USER_ENDPOINT + FORWARD_SLASH + savedUser.getId(),
        HttpMethod.PUT,
        httpEntity,
        User.class
      );

      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(USER2_FIRST_NAME,response.getBody().getFirstName());
      
      Long updatedUserId = savedUser.getId();
      User updatedUser = userRepository.getReferenceById(updatedUserId);
      assertEquals(response.getBody().getId(),updatedUser.getId());
    }
}