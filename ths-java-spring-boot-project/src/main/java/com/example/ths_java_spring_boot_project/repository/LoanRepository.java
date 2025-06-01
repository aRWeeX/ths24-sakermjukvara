package com.example.ths_java_spring_boot_project.repository;

import com.example.ths_java_spring_boot_project.entity.Loan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query("""
        SELECT CASE WHEN b.availableCopies > 0 THEN true ELSE false END
        FROM Book b
        WHERE b.id = :bookId
    """)
    boolean isBookAvailable(@Param("bookId") Long bookId);

    @Query("""
        SELECT l
        FROM Loan l
        WHERE l.user.id = :userId AND l.returnedDate IS NULL
    """)
    List<Loan> findActiveLoansByUser(@Param("userId") Long userId);

    @Query("""
        SELECT l
        FROM Loan l
        WHERE l.dueDate < CURRENT_DATE AND l.returnedDate IS NULL
    """)
    List<Loan> findOverdueLoans();

    @Modifying
    @Transactional
    @Query("""
        UPDATE Loan l
        SET l.returnedDate = CURRENT_TIMESTAMP
        WHERE l.id = :loanId
    """)
    void markLoanAsReturned(@Param("loanId") Long loanId);

    @Query("""
        SELECT COUNT(lb)
        FROM LoanBook lb
        WHERE lb.book.id = :bookId AND lb.loan.returnedDate IS NULL
    """)
    long countActiveLoansByBook(@Param("bookId") Long bookId);

    @Query("""
        SELECT l
        FROM Loan l
        WHERE l.dueDate BETWEEN CURRENT_DATE AND :endDate AND l.returnedDate IS NULL
    """)
    List<Loan> findLoansDueBy(@Param("endDate")LocalDate endDate);
}
