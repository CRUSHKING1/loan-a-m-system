package com.loanapp.emi.util;

import java.math.BigDecimal;

public class PenaltyCalculator {

    private static final BigDecimal DAILY_RATE = new BigDecimal("0.01");
    private static final BigDecimal MAX_PENALTY_PERCENT = new BigDecimal("0.20");

    public static BigDecimal calculate(BigDecimal emiAmount, long daysLate) {

        BigDecimal penalty = emiAmount
                .multiply(DAILY_RATE)
                .multiply(BigDecimal.valueOf(daysLate));

        BigDecimal maxPenalty = emiAmount.multiply(MAX_PENALTY_PERCENT);

        return penalty.min(maxPenalty);
    }
}


