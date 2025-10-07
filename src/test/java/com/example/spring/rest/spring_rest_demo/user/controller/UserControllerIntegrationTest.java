package com.example.spring.rest.spring_rest_demo.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.example.spring.rest.spring_rest_demo.SpringRestDemoApplication;
import com.example.spring.rest.spring_rest_demo.user.model.User;
import com.example.spring.rest.spring_rest_demo.user.repository.UserRepository;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
  classes = SpringRestDemoApplication.class)
@TestPropertySource(
  locations = "classpath:application-test.properties")
public class UserControllerIntegrationTest {
    private final long USER_ID_1 = 1l;
    
    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup(){
        userRepository.deleteAll();
    }

    @Test
    void testSomething(){
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

}