package com.loanapp.loan.dto;



import com.loanapp.common.enums.LoanRejectionReason;
import com.loanapp.common.enums.LoanStatus;
import com.loanapp.common.enums.LoanType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoanResponseDto {
    private Long loanId;
    private LoanType loanType;
    private Double loanAmount;
    private Integer tenureMonths;
    private Double interestRate;
    private Double emiAmount;
    private LoanStatus status;
    private LoanRejectionReason rejectionReason;
}
