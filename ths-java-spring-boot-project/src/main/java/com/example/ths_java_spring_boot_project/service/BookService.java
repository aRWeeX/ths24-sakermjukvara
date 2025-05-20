package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.AuthorDto;
import com.example.ths_java_spring_boot_project.dto.BookDto;
import com.example.ths_java_spring_boot_project.dto.BookWithDetailsDto;
import com.example.ths_java_spring_boot_project.entity.Book;
import com.example.ths_java_spring_boot_project.repository.AuthorRepository;
import com.example.ths_java_spring_boot_project.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::getBookDto)
                .collect(Collectors.toList());
    }

    public Optional<BookDto> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::getBookDto);
    }

    public BookDto saveBook(BookDto bookDto) {
        Book savedBook = bookRepository.save(getBook(bookDto));
        return getBookDto(savedBook);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    private BookDto getBookDto(Book book) {
        BookDto bookDto = new BookDto(
                book.getId(),
                book.getTitle(),
                book.getPublicationYear(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                book.getAuthorId()
        );

        return bookDto;
    }

    private Book getBook(BookDto bookDto) {
        Book book = new Book(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getPublicationYear(),
                bookDto.getAvailableCopies(),
                bookDto.getTotalCopies()
        );

        return book;
    }

    public BookWithDetailsDto getBookWithDetailsDto(BookDto bookDto) {
        AuthorDto authorDto = authorRepository.findById(bookDto.getAuthorId())
                .map(author -> new AuthorDto(
                        author.getId(),
                        author.getFirstName(),
                        author.getLastName(),
                        author.getBirthYear(),
                        author.getNationality()
                ))
                .orElse(null);

        BookWithDetailsDto bookWithDetailsDto = new BookWithDetailsDto(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getPublicationYear(),
                bookDto.getAvailableCopies(),
                bookDto.getTotalCopies(),
                authorDto
        );

        return bookWithDetailsDto;
    }
}
