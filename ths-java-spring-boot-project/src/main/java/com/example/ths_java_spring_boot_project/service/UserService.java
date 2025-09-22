package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.UserRequestDto;
import com.example.ths_java_spring_boot_project.dto.UserResponseDto;
import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.exception.ResourceNotFoundException;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import org.hibernate.service.spi.ServiceException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> page;
        page = userRepository.findAll(pageable);
        return page.map(this::toUserResponseDto);
    }

//    public List<UserResponseDto> getAllUsers() {
//        return userRepository.findAll().stream()
//                .map(this::toUserResponseDto)
//                .toList();
//    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toUserResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @PreAuthorize("hasRole('ADMIN') or #email == principal.username")
    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toUserResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
//        validateUser(userRequestDto);
        String hashedPassword = hashPassword(userRequestDto.getPlainPassword());
        User savedUser = userRepository.save(toUserEntity(userRequestDto, hashedPassword));

        return toUserResponseDto(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateUser(Long id, UserRequestDto updatedUser) {
//        validateUser(updatedUser);
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

    @PreAuthorize("hasRole('ADMIN')")
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
}
