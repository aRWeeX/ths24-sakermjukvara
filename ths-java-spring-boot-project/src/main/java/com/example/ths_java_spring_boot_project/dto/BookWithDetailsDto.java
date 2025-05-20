package com.example.ths_java_spring_boot_project.dto;

public class BookWithDetailsDto {
    private Long id;
    private String title;
    private Integer publicationYear;
    private Integer availableCopies;
    private Integer totalCopies;
    private AuthorDto author;

    public BookWithDetailsDto(Long id, String title, Integer publicationYear,
                              Integer availableCopies, Integer totalCopies,
                              AuthorDto author) {
        this.id = id;
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public AuthorDto getAuthor() {
        return author;
    }
}
