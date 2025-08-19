package com.example.ths_java_spring_boot_project.controller;

import com.example.ths_java_spring_boot_project.dto.BookDto;
import com.example.ths_java_spring_boot_project.dto.BookWithDetailsDto;
import com.example.ths_java_spring_boot_project.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Page<BookDto>> getBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @PageableDefault(size = 10, sort = "title") Pageable pageable
    ) {
        Page<BookDto> books = bookService.getBooks(title, author, pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto savedBook = bookService.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        BookDto savedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.getBookById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<BookWithDetailsDto> getBookWithDetails(@PathVariable Long id) {
        BookDto book = bookService.getBookById(id);
        BookWithDetailsDto bookWithDetailsDto = bookService.getBookWithDetailsDto(book);
        return ResponseEntity.ok(bookWithDetailsDto);
    }
}
