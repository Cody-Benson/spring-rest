package com.example.spring.rest.spring_rest_demo.user.dto;

public class UserReplaceDTO {
    private String firstName;
    private String lastName;
    private String email;
    private int phoneNumber;
    private String password;

    //for request body parsing
    private UserReplaceDTO(){}

    public UserReplaceDTO(UserReplaceDTOBuilder builder){
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.password = builder.password;
    }

    public String getFirstName(){
        return this.firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return this.lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public int getPhoneNumber(){
        return this.phoneNumber;
    }
    public void setPhoneNumber(int phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public static class UserReplaceDTOBuilder {
        public String firstName;
        public String lastName;
        public String email;
        public int phoneNumber;
        public String password;

        public UserReplaceDTOBuilder(){
            this.firstName = "cody";
            this.lastName = "benson";
            this.email = "cody@mail.com";
            this.phoneNumber = 1234567890;
            this.password = "password";
        }

        public UserReplaceDTOBuilder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }

        public UserReplaceDTOBuilder lastName(String lastName){
            this.lastName = lastName;
            return this;
        }

        public UserReplaceDTOBuilder email(String email){
            this.email = email;
            return this;
        }

        public UserReplaceDTOBuilder phoneNumber(int phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserReplaceDTOBuilder password(String password){
            this.password = password;
            return this;
        }

        public UserReplaceDTO build(){
            return new UserReplaceDTO(this);
        }
    }

    public static UserReplaceDTOBuilder builder(){
        return new UserReplaceDTOBuilder();
    }
}
