package com.example.ths_java_spring_boot_project.dto;

import java.util.List;

public class LoanRequestDto {
    private Long userId;
    private List<Long> bookIds;

    public LoanRequestDto() {}

    public LoanRequestDto(Long userId, List<Long> bookIds) {
        this.userId = userId;
        this.bookIds = bookIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }
}
