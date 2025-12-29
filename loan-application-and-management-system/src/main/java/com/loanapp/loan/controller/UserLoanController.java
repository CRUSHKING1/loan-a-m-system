package com.loanapp.loan.controller;

import com.loanapp.common.enums.LoanStatus;


import com.loanapp.loan.dto.LoanApplyRequestDto;
import com.loanapp.loan.dto.LoanPreviewResponseDto;
import com.loanapp.loan.dto.LoanResponseDto;
import com.loanapp.loan.service.LoanService;
import com.loanapp.kyc.exception.KycNotFoundException;
import com.loanapp.user.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user/loans")
@PreAuthorize("hasRole('USER')")
public class UserLoanController {

    private final LoanService loanService;

    public UserLoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // Endpoint to apply for a loan
    @PostMapping("/apply")
    public ResponseEntity<LoanResponseDto> applyLoan(
            @RequestBody LoanApplyRequestDto request) throws UserNotFoundException, KycNotFoundException {
        LoanResponseDto response = loanService.applyLoan(request);  // Correct method call with matching throws
        return ResponseEntity.ok(response);
    }

    // Endpoint to preview a loan (eligibility)
    @PostMapping("/preview")
    public ResponseEntity<LoanPreviewResponseDto> previewLoan(
            @RequestBody LoanApplyRequestDto request) throws UserNotFoundException, KycNotFoundException {
        LoanPreviewResponseDto response = loanService.previewLoan(request);  // Correct method call with matching throws
        return ResponseEntity.ok(response);
    }

    // Endpoint to view all loans of the current user
    @GetMapping
    public ResponseEntity<List<LoanResponseDto>> myLoans() throws UserNotFoundException {
        List<LoanResponseDto> response = loanService.getMyLoans();  // Correct method call with matching throws
        return ResponseEntity.ok(response);
    }

    // Endpoint to view loans by their status for the current user
    @GetMapping("/status")
    public ResponseEntity<List<LoanResponseDto>> myLoansByStatus(
            @RequestParam LoanStatus status) throws UserNotFoundException {
        List<LoanResponseDto> response = loanService.getMyLoansByStatus(status);  // Correct method call with matching throws
        return ResponseEntity.ok(response);
    }

    
}
