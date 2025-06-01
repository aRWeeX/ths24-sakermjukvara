package com.example.ths_java_spring_boot_project.service;

import com.example.ths_java_spring_boot_project.dto.LoanRequestDto;
import com.example.ths_java_spring_boot_project.dto.LoanResponseDto;
import com.example.ths_java_spring_boot_project.entity.Book;
import com.example.ths_java_spring_boot_project.entity.Loan;
import com.example.ths_java_spring_boot_project.entity.LoanBook;
import com.example.ths_java_spring_boot_project.entity.User;
import com.example.ths_java_spring_boot_project.repository.BookRepository;
import com.example.ths_java_spring_boot_project.repository.LoanRepository;
import com.example.ths_java_spring_boot_project.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
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

    public LoanResponseDto createLoan(LoanRequestDto loanRequestDto) {
        User user = userRepository.findById(loanRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Book> books = bookRepository.findAllById(loanRequestDto.getBookIds());

        for (Book book : books) {
            if (book.getAvailableCopies() <= 0) {
                throw new IllegalStateException("Book not available: " + book.getTitle());
            }

            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBorrowedDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14));

        List<LoanBook> loanBooks = books.stream()
                .map(book -> {
                    LoanBook loanBook = new LoanBook();
                    loanBook.setLoan(loan);
                    loanBook.setBook(book);
                    return loanBook;
                })
                .toList();

        loan.setLoanBooks(loanBooks);
        loanRepository.save(loan);
        return getLoanResponseDto(loan);
    }

    public LoanResponseDto returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        loanRepository.markLoanAsReturned(loanId);

        for (LoanBook loanBook : loan.getLoanBooks()) {
            Book book = loanBook.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }

        loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new EntityNotFoundException("Loan not found"));

        return getLoanResponseDto(loan);
    }

    private LoanResponseDto getLoanResponseDto(Loan loan) {
        LoanResponseDto loanResponseDto = new LoanResponseDto(
                loan.getId(),
                loan.getUser().getId(),
                loan.getUser().getEmail(),
                loan.getLoanBooks().stream()
                        .map(lb -> lb.getBook().getTitle())
                        .toList(),
                loan.getBorrowedDate(),
                loan.getDueDate(),
                loan.getReturnedDate()
        );

        return loanResponseDto;
    }

    private Loan getLoan(LoanRequestDto loanRequestDto) {
        Loan loan = new Loan();

        User user = userRepository.findById(loanRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        loan.setUser(user);
        List<LoanBook> loanBooks = new ArrayList<>();

        for (Long bookId : loanRequestDto.getBookIds()) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new IllegalStateException("Book not found"));

            LoanBook loanBook = new LoanBook(loan, book);
            loanBooks.add(loanBook);
        }

        loan.setLoanBooks(loanBooks);
        loan.setBorrowedDate(LocalDateTime.now());
        loan.setDueDate(LocalDateTime.now().plusDays(14));
        return loan;
    }
}
