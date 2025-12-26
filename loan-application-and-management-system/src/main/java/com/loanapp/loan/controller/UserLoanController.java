package com.loanapp.loan.controller;

import com.loanapp.common.enums.LoanStatus;
import com.loanapp.loan.dto.LoanApplyRequestDto;
import com.loanapp.loan.dto.LoanPreviewResponseDto;
import com.loanapp.loan.dto.LoanResponseDto;
import com.loanapp.loan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/loans")
public class UserLoanController {

    private final LoanService loanService;

    public UserLoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // Endpoint for applying a loan
    @PostMapping("/apply")
    public ResponseEntity<LoanResponseDto> applyLoan(@RequestParam Long userId, @RequestBody LoanApplyRequestDto request) {
        LoanResponseDto response = loanService.applyLoan(userId, request);
        return ResponseEntity.ok(response);
    }

    // Endpoint for previewing a loan (eligibility check without applying)
    @PostMapping("/preview")
    public ResponseEntity<LoanPreviewResponseDto> previewLoan(@RequestParam Long userId, @RequestBody LoanApplyRequestDto request) {
        LoanPreviewResponseDto preview = loanService.previewLoan(userId, request);
        return ResponseEntity.ok(preview);
    }

    // Endpoint to view all loans for a user (all statuses)
    @GetMapping("/view")
    public ResponseEntity<List<LoanResponseDto>> viewLoans(@RequestParam Long userId) {
        List<LoanResponseDto> loans = loanService.getLoansByUser(userId);
        return ResponseEntity.ok(loans);
    }

    // Endpoint to view active loans for a user
    @GetMapping("/view/active")
    public ResponseEntity<List<LoanResponseDto>> viewActiveLoans(@RequestParam Long userId) {
        List<LoanResponseDto> loans = loanService.getLoansByUserAndStatus(userId, LoanStatus.ACTIVE);
        return ResponseEntity.ok(loans);
    }

    // Endpoint to view closed loans for a user
    @GetMapping("/view/closed")
    public ResponseEntity<List<LoanResponseDto>> viewClosedLoans(@RequestParam Long userId) {
        List<LoanResponseDto> loans = loanService.getLoansByUserAndStatus(userId, LoanStatus.CLOSED);
        return ResponseEntity.ok(loans);
    }
}
