package com.loanapp.loan.dto;



import com.loanapp.common.enums.LoanType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoanApplyRequestDto {
    private LoanType loanType;
    private Double loanAmount;
    private Integer tenureMonths;
}
