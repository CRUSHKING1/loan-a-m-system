package com.loanapp.loan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoanPreviewResponseDto {
    private Double interestRate;
    private Double emiAmount;
    private Double totalPayable;
}
