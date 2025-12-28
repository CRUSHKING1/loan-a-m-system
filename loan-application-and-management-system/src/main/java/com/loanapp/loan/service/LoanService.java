package com.loanapp.loan.service;

import com.loanapp.common.enums.LoanStatus;
import com.loanapp.kyc.exception.KycNotFoundException;
import com.loanapp.loan.dto.*;
import com.loanapp.user.exception.UserNotFoundException;

import java.util.List;

public interface LoanService {

    LoanResponseDto applyLoan(Long userId, LoanApplyRequestDto request) throws UserNotFoundException, KycNotFoundException; // For applying a loan

    LoanPreviewResponseDto previewLoan(Long userId, LoanApplyRequestDto request) throws UserNotFoundException, KycNotFoundException; // For previewing the loan eligibility

    LoanResponseDto approveOrRejectLoan(Long loanId, LoanApprovalRequestDto request); // For admin to approve/reject

    LoanResponseDto activateLoan(Long loanId); // For admin to activate the loan

    LoanResponseDto closeLoan(Long loanId); // To close the loan

    List<LoanResponseDto> getLoansByUser(Long userId); // View loans for a user

    List<LoanResponseDto> getLoansByUserAndStatus(Long userId, LoanStatus status); // Filter loans by user and status

    List<LoanResponseDto> getAllLoans(); // Admin view of all loans

    List<LoanResponseDto> getLoansByStatus(LoanStatus status); // Filter loans by status (for admin view)

}
