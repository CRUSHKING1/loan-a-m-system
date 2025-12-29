package com.loanapp.loan.service;

import com.loanapp.common.enums.LoanStatus;
import com.loanapp.kyc.exception.KycNotFoundException;
import com.loanapp.loan.dto.*;
import com.loanapp.user.exception.UserNotFoundException;

import java.util.List;

public interface LoanService {

    LoanResponseDto applyLoan(LoanApplyRequestDto request) throws UserNotFoundException, KycNotFoundException; // For applying a loan

    LoanPreviewResponseDto previewLoan(LoanApplyRequestDto request) throws UserNotFoundException, KycNotFoundException; // For previewing the loan eligibility

    LoanResponseDto approveOrRejectLoan(Long loanId, LoanApprovalRequestDto request); // For admin to approve/reject loan

    LoanResponseDto activateLoan(Long loanId); // For admin to activate the loan

  

    List<LoanResponseDto> getMyLoans() throws UserNotFoundException; // For viewing loans of the current user

    List<LoanResponseDto> getMyLoansByStatus(LoanStatus status) throws UserNotFoundException; // Filter loans by user and status (matching implementation)

    List<LoanResponseDto> getAllLoans(); // Admin view of all loans

    List<LoanResponseDto> getLoansByStatus(LoanStatus status); // Filter loans by status (for admin view)
}
