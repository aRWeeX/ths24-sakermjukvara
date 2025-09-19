package com.example.ths_java_spring_boot_project.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public record PageDto<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {
    // Static factory method to create PageDto from Spring Page
    public static <T> PageDto<T> fromPage(Page<T> page) {
        return new PageDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
