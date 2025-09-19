package com.example.ths_java_spring_boot_project.util;

import com.example.ths_java_spring_boot_project.dto.PageDto;
import org.springframework.data.domain.Page;

public class PageUtils {
    private PageUtils() {
        // Private constructor prevents instantiation
    }

    public static <T> PageDto<T> toDto(Page<T> page) {
        return PageDto.fromPage(page);
    }
}
