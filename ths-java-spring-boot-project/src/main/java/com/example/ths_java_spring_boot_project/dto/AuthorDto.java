package com.example.ths_java_spring_boot_project.dto;

public class AuthorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer birthYear;
    private String nationality;

    public AuthorDto(Long id, String firstName, String lastName, Integer birthYear, String nationality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.nationality = nationality;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public String getNationality() {
        return nationality;
    }
}
