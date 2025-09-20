package com.example.ths_java_spring_boot_project.dto;

import java.time.LocalDateTime;
import java.util.List;

public record LoanResponseDto(
        Long loanId,
        Long userId,
        String firstName,
        String lastName,
        String email,
        List<String> loanBooks,
        LocalDateTime borrowDate,
        LocalDateTime dueDate,
        LocalDateTime returnDate
) {}
