package com.loanapp.loan.util;



import com.loanapp.common.enums.LoanType;
import com.loanapp.loan.dto.LoanApplyRequestDto;
import com.loanapp.loan.exception.LoanException;

public class LoanRules {

    public static void validateAmount(LoanApplyRequestDto req) {
        if (req.getLoanType() == LoanType.PERSONAL && req.getLoanAmount() > 300_000)
            throw new LoanException("Personal loan max is 3,00,000");

        if (req.getLoanType() == LoanType.EDUCATION && req.getLoanAmount() > 1_000_000)
            throw new LoanException("Education loan max is 10,00,000");

        if (req.getLoanType() == LoanType.BUSINESS && req.getLoanAmount() > 2_000_000)
            throw new LoanException("Business loan max is 20,00,000");
    }

    public static void validateTenure(LoanApplyRequestDto req) {
        if (req.getTenureMonths() <= 0)
            throw new LoanException("Invalid tenure");
    }
}
