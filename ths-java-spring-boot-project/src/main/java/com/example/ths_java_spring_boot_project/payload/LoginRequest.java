package com.example.ths_java_spring_boot_project.payload;

public class LoginRequest {
    // Fields
    private String username;
    private String password;
    private boolean rememberMe;

    // Constructors
    public LoginRequest() {}

    // Getters & Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
