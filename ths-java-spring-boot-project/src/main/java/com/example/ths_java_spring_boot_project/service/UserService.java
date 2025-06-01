package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.UserRequestDto;
import com.example.ths_java_spring_boot_project.dto.UserResponseDto;
import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::getUserResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::getUserResponseDto);
    }

    public UserResponseDto saveUser(UserRequestDto userRequestDto) {
        String hashedPassword = hashPassword(userRequestDto.getPlainPassword());
        User savedUser = userRepository.save(getUser(userRequestDto, hashedPassword));
        return getUserResponseDto(savedUser);
    }

    public Optional<UserResponseDto> updateUserById(Long id, UserRequestDto userRequestDto) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setEmail(userRequestDto.getEmail());

        if (userRequestDto.getPlainPassword() != null && !userRequestDto.getPlainPassword().isBlank()) {
            user.setHashedPassword(hashPassword(userRequestDto.getPlainPassword()));
        }

        User savedUser = userRepository.save(user);
        return Optional.of(getUserResponseDto(savedUser));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponseDto getUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRegistrationDate()
        );

        return userResponseDto;
    }

    private User getUser(UserRequestDto userRequestDto, String hashedPassword) {
        User user = new User(
                userRequestDto.getFirstName(),
                userRequestDto.getLastName(),
                userRequestDto.getEmail(),
                hashedPassword,
                LocalDateTime.now()
        );

        return user;
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
