package com.example.ths_java_spring_boot_project.controller;

import com.example.ths_java_spring_boot_project.dto.BookDto;
import com.example.ths_java_spring_boot_project.dto.BookWithDetailsDto;
import com.example.ths_java_spring_boot_project.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        Optional<BookDto> book = bookService.getBookById(id);

        return book
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto savedBook = bookService.saveBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBookById(@PathVariable Long id, @RequestBody BookDto bookDto) {
        BookDto updatedBookDto = new BookDto(
                id,
                bookDto.getTitle(),
                bookDto.getPublicationYear(),
                bookDto.getAvailableCopies(),
                bookDto.getTotalCopies(),
                bookDto.getAuthorId()
        );

        Optional<BookDto> existingBook = bookService.getBookById(id);

        return existingBook
                .map(existingBookDto -> {
                    BookDto savedBookDto = bookService.saveBook(updatedBookDto);
                    return ResponseEntity.ok(savedBookDto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        Optional<BookDto> existingBook = bookService.getBookById(id);

        return existingBook
                .map(existingBookDto -> {
                    bookService.deleteBookById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BookWithDetailsDto> getBookWithDetails(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(bookDto -> ResponseEntity.ok(bookService.getBookWithDetailsDto(bookDto)))
                .orElse(ResponseEntity.notFound().build());
    }
}
