package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.AuthorDto;
import com.example.ths_java_spring_boot_project.entity.Author;
import com.example.ths_java_spring_boot_project.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::getAuthorDto)
                .collect(Collectors.toList());
    }

    public Optional<AuthorDto> getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(this::getAuthorDto);
    }

    public AuthorDto saveAuthor(AuthorDto authorDto) {
        Author savedAuthor = authorRepository.save(getAuthor(authorDto));
        return getAuthorDto(savedAuthor);
    }

    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id);
    }

    private AuthorDto getAuthorDto(Author author) {
        AuthorDto authorDto = new AuthorDto(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                author.getBirthYear(),
                author.getNationality()
        );

        return authorDto;
    }

    private Author getAuthor(AuthorDto authorDto) {
        Author author = new Author(
                authorDto.getId(),
                authorDto.getFirstName(),
                authorDto.getLastName(),
                authorDto.getBirthYear(),
                authorDto.getNationality()
        );

        return author;
    }
}
