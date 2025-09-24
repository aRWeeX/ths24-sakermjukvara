package com.example.ths_java_spring_boot_project.exception;

import com.example.ths_java_spring_boot_project.payload.ApiResponse;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, List<String>> errors = new HashMap<>();

        // Field-specific errors
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.computeIfAbsent(error.getField(), k -> new ArrayList<>())
                    .add(error.getDefaultMessage());
        });

        // Global errors (class-level, cross-field)
        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            errors.computeIfAbsent(error.getObjectName(), k -> new ArrayList<>())
                    .add(error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Validation failed", errors));
    }

    // Handle custom exceptions
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponse<Void>> handleServiceException(ServiceException ex) {
        logger.error("Service Exception: {}", ex.getMessage(), ex);

        ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // Handle all other exceptions (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        logger.error("Unexpected error occurred", ex);

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Something went wrong. Please try again later."
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
