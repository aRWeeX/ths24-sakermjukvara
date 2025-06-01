package com.example.ths_java_spring_boot_project.dto;

public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String plainPassword;

    public UserRequestDto(String firstName, String lastName, String email,
                          String plainPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.plainPassword = plainPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }
}
