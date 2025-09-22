package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.LoanRequestDto;
import com.example.ths_java_spring_boot_project.dto.LoanResponseDto;
import com.example.ths_java_spring_boot_project.entity.Book;
import com.example.ths_java_spring_boot_project.entity.Loan;
import com.example.ths_java_spring_boot_project.entity.LoanBook;
import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.exception.ResourceNotFoundException;
import com.example.ths_java_spring_boot_project.repository.BookRepository;
import com.example.ths_java_spring_boot_project.repository.LoanRepository;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@PreAuthorize("hasRole('ADMIN')")
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, UserRepository userRepository,
                       BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Page<LoanResponseDto> getAllLoans(Pageable pageable) {
        return loanRepository.findAll(pageable)
                .map(this::toLoanResponseDto);
    }

    @Transactional
    public LoanResponseDto issueLoan(LoanRequestDto loanRequestDto) {
        validateLoan(loanRequestDto);

        User user = userRepository.findById(loanRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + loanRequestDto.getUserId()));

        List<Book> books = bookRepository.findAllById(loanRequestDto.getBookIds());

        if (books.size() != loanRequestDto.getBookIds().size()) {
            throw new ResourceNotFoundException("One or more books were not found");
        }

        for (Book book : books) {
            if (book.getAvailableCopies() <= 0) {
                throw new IllegalStateException("Book not available: " + book.getTitle());
            }

            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookRepository.save(book);
        }

        Loan loan = toLoanEntity(user, books);
        loanRepository.save(loan);
        return toLoanResponseDto(loan);
    }

    @Transactional
    public LoanResponseDto returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + loanId));

        loanRepository.markLoanAsReturned(loanId);

        for (LoanBook loanBook : loan.getLoanBooks()) {
            Book book = loanBook.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }

        return toLoanResponseDto(loan);
    }

    private LoanResponseDto toLoanResponseDto(Loan loan) {
        User user = loan.getUser();

        return new LoanResponseDto(
                loan.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                loan.getLoanBooks().stream()
                        .map(loanBook -> loanBook.getBook().getTitle())
                        .toList(),
                loan.getBorrowedDate(),
                loan.getDueDate(),
                loan.getReturnedDate()
        );
    }

    private Loan toLoanEntity(User user, List<Book> books) {
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBorrowedDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14));

        List<LoanBook> loanBooks = books.stream()
                .map(book -> new LoanBook(loan, book))
                .toList();

        loan.setLoanBooks(loanBooks);
        return loan;
    }

    private void validateLoan(LoanRequestDto loanRequestDto) {
        if (loanRequestDto.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (!userRepository.existsById(loanRequestDto.getUserId())) {
            throw new IllegalStateException("User does not exist with ID: " + loanRequestDto.getUserId());
        }

        List<Long> bookIds = loanRequestDto.getBookIds();

        if (bookIds == null || bookIds.isEmpty()) {
            throw new IllegalArgumentException("At least one book ID is required");
        }

        Set<Long> seenBookIds = new HashSet<>();

        for (Long bookId : bookIds) {
            if (!seenBookIds.add(bookId)) {
                throw new IllegalArgumentException("Duplicate book ID in loan: " + bookId);
            }

            if (!bookRepository.existsById(bookId)) {
                throw new IllegalStateException("Book does not exist with ID: " + bookId);
            }
        }
    }
}
