package com.example.ths_java_spring_boot_project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "loan_books")
public class LoanBook {
    @EmbeddedId
    private LoanBookId id;

    @ManyToOne
    @MapsId("loanId")
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    public LoanBook() {}

    public LoanBook(Loan loan, Book book) {
        this.loan = loan;
        this.book = book;
        this.id = new LoanBookId(loan.getId(), book.getId());
    }

    public LoanBookId getId() {
        return id;
    }

    public void setId(LoanBookId id) {
        this.id = id;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof LoanBook)) return false;

        LoanBook other = (LoanBook) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
