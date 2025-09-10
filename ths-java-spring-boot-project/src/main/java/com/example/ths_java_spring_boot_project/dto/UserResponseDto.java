package com.example.ths_java_spring_boot_project.dto;

import java.time.LocalDateTime;

/**
 * @param id Fields
 */
public record UserResponseDto(Long id, String firstName, String lastName, String email,
                              LocalDateTime registrationDate) {
    // Constructors
}
