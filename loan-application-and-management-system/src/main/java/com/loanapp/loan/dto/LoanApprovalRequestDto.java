package com.loanapp.loan.dto;


import com.loanapp.common.enums.LoanRejectionReason;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanApprovalRequestDto {
    
    private boolean approved;  // Indicates whether the loan is approved or not
    
    private String remarks;    // Optional remarks provided by admin

    private LoanRejectionReason rejectionReason; // Set when the loan is rejected

    // A helper method to retrieve the rejection reason only when the loan is rejected
    public LoanRejectionReason getRejectionReason() {
        if (!approved) {
            return rejectionReason;  // Only return rejection reason if the loan is not approved
        }
        return null;  // Return null if the loan is approved
    }
}

