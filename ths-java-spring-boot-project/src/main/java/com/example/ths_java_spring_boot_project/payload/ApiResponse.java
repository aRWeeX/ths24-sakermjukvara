package com.example.ths_java_spring_boot_project.payload;

public class ApiResponse<T> {
    // Fields
    private String message;
    private T data;

    // Constructors
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    // Getters & Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
