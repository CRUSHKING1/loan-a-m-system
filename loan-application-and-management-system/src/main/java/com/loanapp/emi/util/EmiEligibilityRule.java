package com.loanapp.emi.util;

import java.math.BigDecimal;

public class EmiEligibilityRule {

    public static boolean validate(double monthlyIncome, BigDecimal emiAmount) {

        BigDecimal maxAllowed =
                BigDecimal.valueOf(monthlyIncome)
                        .multiply(new BigDecimal("0.40"));

        if (emiAmount.compareTo(maxAllowed) > 0) {
           return false;
        }
        return true;
    }
}