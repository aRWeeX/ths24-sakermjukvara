package com.example.ths_java_spring_boot_project.dto;

public class BookDto {
    private Long id;
    private String title;
    private Integer publicationYear;
    private Integer availableCopies;
    private Integer totalCopies;
    private Long authorId;

    public BookDto() {}

    public BookDto(Long id, String title, Integer publicationYear,
                   Integer availableCopies, Integer totalCopies, Long authorId) {
        this.id = id;
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.authorId = authorId;
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

    public Long getAuthorId() {
        return authorId;
    }
}
