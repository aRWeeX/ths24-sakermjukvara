package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.AuthorDto;
import com.example.ths_java_spring_boot_project.dto.BookDto;
import com.example.ths_java_spring_boot_project.dto.BookWithDetailsDto;
import com.example.ths_java_spring_boot_project.entity.Author;
import com.example.ths_java_spring_boot_project.entity.Book;
import com.example.ths_java_spring_boot_project.exception.ResourceNotFoundException;
import com.example.ths_java_spring_boot_project.repository.AuthorRepository;
import com.example.ths_java_spring_boot_project.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Page<BookDto> getBooks(String title, String author, Pageable pageable) {
        Page<Book> page;

        if (title != null && !title.isEmpty()) {
            page = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (author != null && !author.isEmpty()) {
            page = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
        } else {
            page = bookRepository.findAll(pageable);
        }

        return page.map(this::toBookDto);
    }

    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::toBookDto)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
    }

    public BookDto createBook(BookDto bookDto) {
        validateBook(bookDto);
        Book savedBook = bookRepository.save(toBookEntity(bookDto));
        return toBookDto(savedBook);
    }

    public BookDto updateBook(Long id, BookDto updatedBook) {
        validateBook(updatedBook);
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            throw new ResourceNotFoundException("Book not found with ID: " + id);
        }

        Book book = optionalBook.get();
        book.setTitle(updatedBook.getTitle());
        book.setPublicationYear(updatedBook.getPublicationYear());
        book.setAvailableCopies(updatedBook.getAvailableCopies());
        book.setTotalCopies(updatedBook.getTotalCopies());

        Author author = authorRepository.findById(updatedBook.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Author not found with ID: " + updatedBook.getAuthorId()));

        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return toBookDto(savedBook);
    }

    public void deleteBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book does not exist with ID: " + id);
        }

        bookRepository.deleteById(id);
    }

    private BookDto toBookDto(Book book) {
        Long authorId = null;
        String firstName = null;
        String lastName = null;

        if (book.getAuthor() != null) {
            authorId = book.getAuthor().getId();
            firstName = book.getAuthor().getFirstName();
            lastName = book.getAuthor().getLastName();
        }

        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getPublicationYear(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                authorId,
                firstName,
                lastName
        );
    }

    private Book toBookEntity(BookDto bookDto) {
        Author author = new Author();
        author.setId(bookDto.getAuthorId());

        return new Book(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getPublicationYear(),
                bookDto.getAvailableCopies(),
                bookDto.getTotalCopies(),
                author
        );
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

        return new BookWithDetailsDto(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getPublicationYear(),
                bookDto.getAvailableCopies(),
                bookDto.getTotalCopies(),
                authorDto
        );
    }

    private void validateBook(BookDto bookDto) {
        if (bookDto.getTitle() == null || bookDto.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        // Publication year can be NULL
        if (bookDto.getPublicationYear() != null) {
            int year = bookDto.getPublicationYear();
            int currentYear = Year.now().getValue();

            if (year < 1800 || year > currentYear + 1) {
                throw new IllegalArgumentException("Publication year must be between 1800 and " + (currentYear + 1));
            }
        }

        // Available copies and total copies can be NULL
        Integer available = bookDto.getAvailableCopies();
        Integer total = bookDto.getTotalCopies();

        if (available != null && available < 0) {
            throw new IllegalArgumentException("Available copies cannot be negative");
        }

        if (total != null && total < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative");
        }

        if (available != null && total != null && available > total) {
            throw new IllegalArgumentException("Available copies cannot exceed total copies");
        }

        // Author ID can be NULL
        if (bookDto.getAuthorId() != null) {
            boolean authorExists = authorRepository.existsById(bookDto.getAuthorId());

            if (!authorExists) {
                throw new IllegalArgumentException("Author does not exist with ID: " + bookDto.getAuthorId());
            }
        }
    }
}
