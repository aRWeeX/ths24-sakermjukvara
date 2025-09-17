package com.example.ths_java_spring_boot_project.repository;

import com.example.ths_java_spring_boot_project.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @NonNull
    Page<Book> findAll(@NonNull Pageable pageable);
    
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
}
