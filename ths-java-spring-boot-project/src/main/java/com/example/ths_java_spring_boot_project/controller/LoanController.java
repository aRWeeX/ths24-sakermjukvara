package com.example.ths_java_spring_boot_project.controller;

import com.example.ths_java_spring_boot_project.payload.ApiResponse;
import com.example.ths_java_spring_boot_project.dto.LoanRequestDto;
import com.example.ths_java_spring_boot_project.dto.LoanResponseDto;
import com.example.ths_java_spring_boot_project.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoanResponseDto>> issueLoan(@RequestBody LoanRequestDto loanRequestDto) {
        LoanResponseDto loanResponseDto = loanService.issueLoan(loanRequestDto);
        return new ResponseEntity<>(new ApiResponse<>("Loan issued", loanResponseDto), (HttpStatus.CREATED));
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<Map<String, Object>> returnLoan(@PathVariable Long loanId) {
        LoanResponseDto loanResponseDto = loanService.returnLoan(loanId);

        if (loanResponseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Loan not found with ID: " + loanId));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Loan returned with ID: " + loanId,
                "loan", loanResponseDto
        ));
    }
}
