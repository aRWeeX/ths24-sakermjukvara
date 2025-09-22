package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.AuthorDto;
import com.example.ths_java_spring_boot_project.entity.Author;
import com.example.ths_java_spring_boot_project.exception.ResourceNotFoundException;
import com.example.ths_java_spring_boot_project.repository.AuthorRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<AuthorDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        try {
            return authors.stream()
                    .map(this::toAuthorDto)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("An error occurred while retrieving authors", e);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public AuthorDto getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(this::toAuthorDto)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with ID: " + id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AuthorDto createAuthor(AuthorDto authorDto) {
        validateAuthor(authorDto);
        Author savedAuthor = authorRepository.save(toAuthorEntity(authorDto));
        return toAuthorDto(savedAuthor);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AuthorDto updateAuthor(Long id, AuthorDto updatedAuthor) {
        validateAuthor(updatedAuthor);
        Optional<Author> optionalAuthor = authorRepository.findById(id);

        if (optionalAuthor.isEmpty()) {
            throw new ResourceNotFoundException("Author not found with ID: " + id);
        }

        Author author = optionalAuthor.get();
        author.setFirstName(updatedAuthor.getFirstName());
        author.setLastName(updatedAuthor.getLastName());
        author.setBirthYear(updatedAuthor.getBirthYear());
        author.setNationality(updatedAuthor.getNationality());

        Author savedAuthor = authorRepository.save(author);
        return toAuthorDto(savedAuthor);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAuthorById(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author does not exist with ID: " + id);
        }

        authorRepository.deleteById(id);
    }

    private AuthorDto toAuthorDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthYear(),
                author.getNationality()
        );
    }

    private Author toAuthorEntity(AuthorDto authorDto) {
        return new Author(
                authorDto.getId(),
                authorDto.getFirstName(),
                authorDto.getLastName(),
                authorDto.getBirthYear(),
                authorDto.getNationality()
        );
    }

    private void validateAuthor(AuthorDto authorDto) {
        if (authorDto.getFirstName() == null || authorDto.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (authorDto.getLastName() == null || authorDto.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        // Birth year can be NULL
        if (authorDto.getBirthYear() != null) {
            int year = authorDto.getBirthYear();
            int currentYear = Year.now().getValue();

            if (year < 1750 || year > currentYear) {
                throw new IllegalArgumentException("Birth year must be between 1750 and " + currentYear);
            }
        }

        // Nationality can be NULL
        String nationality = authorDto.getNationality();

        if (nationality != null) {
            nationality = nationality.trim();
        }
    }
}
