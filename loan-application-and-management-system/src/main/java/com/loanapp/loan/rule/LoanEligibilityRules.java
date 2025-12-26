package com.loanapp.loan.rule;

import org.springframework.stereotype.Component;

import com.loanapp.common.enums.LoanType;
import com.loanapp.loan.exception.LoanException;

@Component
public class LoanEligibilityRules {

    public void validateLoanLimits(LoanType type, double amount, int tenure) {
        LoanTypeConfig cfg = LoanTypeConfig.from(type);

        if (amount < cfg.getMinAmount() || amount > cfg.getMaxAmount())
            throw new LoanException("Amount out of range");

        if (tenure < cfg.getMinTenure() || tenure > cfg.getMaxTenure())
            throw new LoanException("Tenure out of range");
    }

    public void validatePerson(int creditScore, double income, int age) {
        if (creditScore < 600) throw new LoanException("Low credit score");
        if (income < 15000) throw new LoanException("Low income");
        if (age < 21 || age > 65) throw new LoanException("Age not eligible");
    }
}
