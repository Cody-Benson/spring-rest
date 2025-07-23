package com.example.spring.rest.spring_rest_demo.user.exception;

public class ConflictException extends RuntimeException{
    
    public ConflictException(String message){
        super(message);
    }
}
