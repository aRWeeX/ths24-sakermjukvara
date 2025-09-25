package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.UserRequestDto;
import com.example.ths_java_spring_boot_project.dto.UserResponseDto;
import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.exception.ResourceNotFoundException;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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

    @PreAuthorize("hasRole('ADMIN') or #email == principal.username")
    public User getUserByEmailEntity(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User savedUser = userRepository.save(toUserEntity(userRequestDto));

        return toUserResponseDto(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateUser(Long id, UserRequestDto updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }

        User user = optionalUser.get();

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setHashedPassword(updatedUser.getPlainPassword());

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

    @PreAuthorize("hasRole('ADMIN')")
    public User save(UserRequestDto userDto) {
        return userRepository.save(toUserEntity(userDto));
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

    private User toUserEntity(UserRequestDto userRequestDto) {
        return new User(
                userRequestDto.getFirstName(),
                userRequestDto.getLastName(),
                userRequestDto.getEmail(),
                userRequestDto.getPlainPassword()
        );
    }
}
