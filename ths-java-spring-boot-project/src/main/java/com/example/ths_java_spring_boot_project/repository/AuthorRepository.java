package com.example.ths_java_spring_boot_project.repository;

import com.example.ths_java_spring_boot_project.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {}
