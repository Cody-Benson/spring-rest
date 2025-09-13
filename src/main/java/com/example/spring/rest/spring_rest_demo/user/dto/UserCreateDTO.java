package com.example.spring.rest.spring_rest_demo.user.dto;

public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private int phoneNumber;
    private String password;

    //for request body parsing
    private UserCreateDTO(){};

    public UserCreateDTO(UserCreateDTOBuilder builder){
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

    public static class UserCreateDTOBuilder{
        private String firstName;
        private String lastName;
        private String email;
        private int phoneNumber;
        private String password;

        public UserCreateDTOBuilder(){
            this.firstName = "cody";
            this.lastName = "benson";
            this.email = "cody@mail.com";
            this.phoneNumber = 1234567890;
            this.password = "password";
        }

        public UserCreateDTOBuilder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }

        public UserCreateDTOBuilder lastName(String lastName){
            this.lastName = lastName;
            return this;
        }

        public UserCreateDTOBuilder email(String email){
            this.email = email;
            return this;
        }

        public UserCreateDTOBuilder phoneNumber(int phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserCreateDTOBuilder password(String password){
            this.password = password;
            return this;
        }

        public UserCreateDTO build(){
            return new UserCreateDTO(this);
        }
    }

    public static UserCreateDTOBuilder builder(){
        return new UserCreateDTOBuilder();
    }
}
