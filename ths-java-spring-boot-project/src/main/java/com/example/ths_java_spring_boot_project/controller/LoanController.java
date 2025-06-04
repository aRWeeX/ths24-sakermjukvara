package com.example.ths_java_spring_boot_project.controller;

import com.example.ths_java_spring_boot_project.dto.LoanRequestDto;
import com.example.ths_java_spring_boot_project.dto.LoanResponseDto;
import com.example.ths_java_spring_boot_project.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public ResponseEntity<LoanResponseDto> issueLoan(@RequestBody LoanRequestDto loanRequestDto) {
        LoanResponseDto loanResponseDto = loanService.issueLoan(loanRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(loanResponseDto);
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanResponseDto> returnLoan(@PathVariable Long loanId) {
        LoanResponseDto loanResponseDto = loanService.returnLoan(loanId);
        return ResponseEntity.ok(loanResponseDto);
    }
}
