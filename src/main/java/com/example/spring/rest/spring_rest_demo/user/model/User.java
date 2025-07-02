package com.example.spring.rest.spring_rest_demo.user.model;

public class User {
    private String firstName;

    public User(String firstName){
        this.firstName = firstName;
    }
    
    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
}