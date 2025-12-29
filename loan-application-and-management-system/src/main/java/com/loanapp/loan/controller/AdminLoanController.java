package com.loanapp.loan.controller;

import com.loanapp.common.enums.LoanStatus;
import com.loanapp.loan.dto.LoanApprovalRequestDto;
import com.loanapp.loan.dto.LoanResponseDto;
import com.loanapp.loan.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/loans")
public class AdminLoanController {

    private final LoanService loanService;

    public AdminLoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // Endpoint for admin to approve or reject a loan
    @PostMapping("/{loanId}/decision")
    public ResponseEntity<LoanResponseDto> approveOrRejectLoan(@PathVariable Long loanId, @RequestBody LoanApprovalRequestDto request) {
        LoanResponseDto loan = loanService.approveOrRejectLoan(loanId, request);
        return ResponseEntity.ok(loan);
    }

    // Endpoint for admin to activate a loan
    @PostMapping("/{loanId}/activate")
    public ResponseEntity<LoanResponseDto> activateLoan(@PathVariable Long loanId) {
        LoanResponseDto loan = loanService.activateLoan(loanId);
        return ResponseEntity.ok(loan);
    }

    // Endpoint to view all loans for the admin
    @GetMapping("/view")
    public ResponseEntity<List<LoanResponseDto>> viewAllLoans() {
        List<LoanResponseDto> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }

    // Endpoint for admin to view loans by status (e.g., active, closed, applied, etc.)
    @GetMapping("/view/status")
    public ResponseEntity<List<LoanResponseDto>> viewLoansByStatus(@RequestParam LoanStatus status) {
        List<LoanResponseDto> loans = loanService.getLoansByStatus(status);
        return ResponseEntity.ok(loans);
    }
}
