package com.loanapp.loan.rule;

import com.loanapp.common.enums.LoanType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum LoanTypeConfig {

    HOME(LoanType.HOME, 500000, 10000000, 60, 360, 8.5),
    PERSONAL(LoanType.PERSONAL, 10000, 500000, 3, 60, 13.5),
    VEHICLE(LoanType.VEHICLE, 50000, 2000000, 12, 84, 9.5);

    private final LoanType loanType;
    private final double minAmount;
    private final double maxAmount;
    private final int minTenure;
    private final int maxTenure;
    private final double baseInterest;

    public static LoanTypeConfig from(LoanType type) {
        return Arrays.stream(values())
                .filter(c -> c.loanType == type)
                .findFirst()
                .orElseThrow( ()->new IllegalArgumentException("Invalid LoanType: " + type));
    }
}
