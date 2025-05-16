package com.example.ths_java_spring_boot_project.controller;

import com.example.ths_java_spring_boot_project.repository.BookRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    private final BookRepository bookRepository;

    public TestController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/test")
    public String test() {
        return "Spring Boot is running!";
    }

    @GetMapping("/test/database")
    public Map<String, Object> testDatabase() {
        Map<String, Object> result =  new HashMap<>();
        result.put("message", "Database connection successful!");
        result.put("book_count", bookRepository.count());
        return result;
    }
}
