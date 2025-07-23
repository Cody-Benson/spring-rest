package com.example.spring.rest.spring_rest_demo.user.exception;

public class ErrorResponse {
    private int status;
    private String message;
    private String details;
    private long timestamp;

    public ErrorResponse(int status, String message, String details){
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = System.currentTimeMillis();
    }

    public int getStatus(){
        return this.status;
    }
    public void setStatus(int status){
        this.status = status;
    }

    public String getMessage(){
        return this.message;
    }
    public void setMessage(String message){
        this.message = message;
    }

    public String getDetails(){
        return this.details;
    }
    public void setDetails(String details){
        this.details = details;
    }

    public long getTimestamp(){
        return this.timestamp;
    }
    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }
}
