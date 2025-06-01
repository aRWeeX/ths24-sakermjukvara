package com.example.ths_java_spring_boot_project.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LoanBookId implements Serializable {
    private Long loanId;
    private Long bookId;

    public LoanBookId() {}

    public LoanBookId(Long loanId, Long bookId) {
        this.loanId = loanId;
        this.bookId = bookId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof LoanBookId)) return false;

        LoanBookId that = (LoanBookId) o;
        return Objects.equals(loanId, that.loanId) &&
                Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, bookId);
    }
}
