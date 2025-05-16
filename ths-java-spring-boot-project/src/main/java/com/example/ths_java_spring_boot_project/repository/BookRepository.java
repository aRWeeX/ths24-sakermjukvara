package com.example.ths_java_spring_boot_project.repository;

import com.example.ths_java_spring_boot_project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface BookRepository extends JpaRepository<Book, Long> {}
