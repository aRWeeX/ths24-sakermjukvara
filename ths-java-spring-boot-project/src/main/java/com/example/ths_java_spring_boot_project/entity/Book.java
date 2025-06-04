package com.example.ths_java_spring_boot_project.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "available_copies")
    private Integer availableCopies;

    @Column(name = "total_copies")
    private Integer totalCopies;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanBook> loanBooks = new ArrayList<>();

    public Book() {}

    public Book(String title, Integer publicationYear, Integer availableCopies,
                Integer totalCopies, Author author) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.author = author;
    }

    public Book(Long id, String title, Integer publicationYear,
                Integer availableCopies, Integer totalCopies, Author author) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Integer getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<LoanBook> getLoanBooks() {
        return loanBooks;
    }

    public void setLoanBooks(List<LoanBook> loanBooks) {
        this.loanBooks = loanBooks;
    }

    public void addLoanBook(LoanBook loanBook) {
        loanBooks.add(loanBook);
        loanBook.setBook(this);
    }

    public void removeLoanBook(LoanBook loanBook) {
        loanBooks.remove(loanBook);
        loanBook.setBook(null);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publicationYear=" + publicationYear +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                ", author=" + (author != null ? author.getId() : null) +
                '}';
    }
}
