package com.example.ths_java_spring_boot_project.controller;

import com.example.ths_java_spring_boot_project.entity.Author;
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
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Optional<Author> fetchedAuthor = authorService.getAuthorById(id);

        return fetchedAuthor
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorService.saveAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthorById(@PathVariable Long id, @RequestBody Author author) {
        Optional<Author> existingAuthor = authorService.getAuthorById(id);

        return existingAuthor
                .map(authorToUpdate -> {
                    author.setId(id);
                    return ResponseEntity.ok(authorService.saveAuthor(author));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable Long id) {
        Optional<Author> existingAuthor = authorService.getAuthorById(id);

        return existingAuthor
                .map(authorToDelete -> {
                    authorService.deleteAuthorById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
