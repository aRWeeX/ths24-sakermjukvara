package com.example.ths_java_spring_boot_project;

import com.example.ths_java_spring_boot_project.dto.LoanRequestDto;
import com.example.ths_java_spring_boot_project.dto.LoanResponseDto;
import com.example.ths_java_spring_boot_project.entity.Book;
import com.example.ths_java_spring_boot_project.entity.Loan;
import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.repository.BookRepository;
import com.example.ths_java_spring_boot_project.repository.LoanRepository;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import com.example.ths_java_spring_boot_project.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LoanServiceTest {
    private LoanRepository loanRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private LoanService loanService;

    private final PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        userRepository = mock(UserRepository.class);
        bookRepository = mock(BookRepository.class);
        loanService = new LoanService(loanRepository, userRepository, bookRepository);
    }

    public LoanServiceTest(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    public void testIssueLoan_success() {
        // Arrange - Test data
        User user = new User(
                "John",
                "Doe",
                "john.doe@example.com",
                passwordEncoder.encode(passwordEncoder.encode("password123¤"))
        );

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title");
        book.setAvailableCopies(1);

        Loan loan = new Loan(user, LocalDateTime.now(), LocalDateTime.now().plusDays(14), null);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findAllById(List.of(book.getId()))).thenReturn(List.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // Act - Mock behavior
        LoanRequestDto loanRequestDto = new LoanRequestDto(user.getId(), List.of(book.getId()));
        LoanResponseDto loanResponseDto = loanService.issueLoan(loanRequestDto);

        // Assert
        assertNotNull(loanResponseDto);

        assertEquals(user.getId(), loanResponseDto.userId());
        assertEquals(List.of(book.getTitle()), loanResponseDto.loanBooks());

        assertNotNull(loanResponseDto.borrowDate());
        assertNotNull(loanResponseDto.dueDate());
    }
}
