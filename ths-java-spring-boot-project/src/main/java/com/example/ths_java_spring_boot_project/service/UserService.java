package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.UserRequestDto;
import com.example.ths_java_spring_boot_project.dto.UserResponseDto;
import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.exception.ResourceNotFoundException;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import org.hibernate.service.spi.ServiceException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (users == null) {
            return Collections.emptyList();
        }

        try {
            return users.stream()
                    .map(this::toUserResponseDto)
                    .toList();
        } catch (Exception e) {
            throw new ServiceException("An error occurred while retrieving users", e);
        }
    }

    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toUserResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toUserResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        validateUser(userRequestDto);
        String hashedPassword = hashPassword(userRequestDto.getPlainPassword());
        User savedUser = userRepository.save(toUserEntity(userRequestDto, hashedPassword));

        return toUserResponseDto(savedUser);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto updatedUser) {
        validateUser(updatedUser);
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }

        User user = optionalUser.get();

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());

        if (updatedUser.getPlainPassword() != null && !updatedUser.getPlainPassword().isBlank()) {
            user.setHashedPassword(hashPassword(updatedUser.getPlainPassword()));
        }

        User savedUser = userRepository.save(user);
        return toUserResponseDto(savedUser);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " does not exist.");
        }

        userRepository.deleteById(id);
    }

    private UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRegistrationDate(),
                user.getRole()
        );
    }

    private User toUserEntity(UserRequestDto userRequestDto, String hashedPassword) {
        return new User(
                userRequestDto.getFirstName(),
                userRequestDto.getLastName(),
                userRequestDto.getEmail(),
                hashedPassword
        );
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    private void validateUser(UserRequestDto userRequestDto) {
        if (userRequestDto.getFirstName() == null || userRequestDto.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        if (userRequestDto.getLastName() == null || userRequestDto.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }

        String email = userRequestDto.getEmail();

        if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        String password = userRequestDto.getPlainPassword();

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain letters and numbers");
        }
    }
}
