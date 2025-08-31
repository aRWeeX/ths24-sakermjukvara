package com.example.ths_java_spring_boot_project.repository;

import com.example.ths_java_spring_boot_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Security needs to find users via email
    Optional<User> findByEmail(String email);

    // Useful for checking if email already exists
    boolean existsByEmail(String email);
}
