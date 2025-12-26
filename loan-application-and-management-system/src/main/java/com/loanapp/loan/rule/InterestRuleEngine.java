package com.loanapp.loan.rule;



import org.springframework.stereotype.Component;

import com.loanapp.common.enums.LoanType;

@Component
public class InterestRuleEngine {

    public double calculate(
            LoanType type,
            int creditScore,
            double income,
            int age,
            int tenure) {

        LoanTypeConfig cfg = LoanTypeConfig.from(type);
        double rate = cfg.getBaseInterest();

        if (creditScore >= 800) rate -= 1.0;
        else if (creditScore >= 750) rate -= 0.5;
        else if (creditScore < 650) rate += 1.5;

        if (income >= 100000) rate -= 0.5;
        if (age >= 25 && age <= 45) rate -= 0.25;
        if (age > 55) rate += 0.75;
        if (tenure > 120) rate += 0.5;

        return Math.max(cfg.getBaseInterest() - 2,
                Math.min(rate, cfg.getBaseInterest() + 4));
    }
}
