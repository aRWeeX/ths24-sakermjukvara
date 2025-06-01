package com.example.ths_java_spring_boot_project.dto;

import java.time.LocalDateTime;
import java.util.List;

public class LoanResponseDto {
    private Long loanId;
    private Long userId;
    private String userName;
    private List<String> bookTitles;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;

    public LoanResponseDto() {}

    public LoanResponseDto(Long loanId, Long userId, String userName,
                           List<String> bookTitles, LocalDateTime borrowDate,
                           LocalDateTime dueDate, LocalDateTime returnDate) {
        this.loanId = loanId;
        this.userId = userId;
        this.userName = userName;
        this.bookTitles = bookTitles;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public Long getLoanId() {
        return loanId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getBookTitles() {
        return bookTitles;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }
}
