package com.example.ths_java_spring_boot_project.controller;

import com.example.ths_java_spring_boot_project.dto.AuthorDto;
import com.example.ths_java_spring_boot_project.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {
        Optional<AuthorDto> author = authorService.getAuthorById(id);

        return author
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        AuthorDto savedAuthor = authorService.saveAuthor(authorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthorById(@PathVariable Long id, @RequestBody AuthorDto authorDto) {
        AuthorDto updatedAuthorDto = new AuthorDto(
                id,
                authorDto.getFirstName(),
                authorDto.getLastName(),
                authorDto.getBirthYear(),
                authorDto.getNationality()
        );

        Optional<AuthorDto> existingAuthor = authorService.getAuthorById(id);

        return existingAuthor
                .map(existingAuthorDto -> {
                    AuthorDto savedAuthorDto = authorService.saveAuthor(updatedAuthorDto);
                    return ResponseEntity.ok(savedAuthorDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long id) {
        Optional<AuthorDto> existingAuthor = authorService.getAuthorById(id);

        return existingAuthor
                .map(existingAuthorDto -> {
                    authorService.deleteAuthorById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
