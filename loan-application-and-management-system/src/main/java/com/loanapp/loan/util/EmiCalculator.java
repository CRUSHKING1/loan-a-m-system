package com.loanapp.loan.util;



public class EmiCalculator {

    public static double calculate(double principal,
                                   double annualRate,
                                   int months) {
        double monthlyRate = annualRate / 12 / 100;
        return (principal * monthlyRate *
                Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);
    }
}