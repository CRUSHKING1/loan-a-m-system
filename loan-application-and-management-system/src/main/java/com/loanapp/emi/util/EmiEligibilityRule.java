package com.loanapp.emi.util;

import java.math.BigDecimal;

public class EmiEligibilityRule {

    public static void validate(double monthlyIncome, BigDecimal emiAmount) {

        BigDecimal maxAllowed =
                BigDecimal.valueOf(monthlyIncome)
                        .multiply(new BigDecimal("0.40"));

        if (emiAmount.compareTo(maxAllowed) > 0) {
            throw new RuntimeException("EMI exceeds 40% of monthly income");
        }
    }
}