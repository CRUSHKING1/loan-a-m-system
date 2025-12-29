package com.loanapp.loan.rule;

import org.springframework.stereotype.Component;

import com.loanapp.common.enums.LoanRejectionReason;
import com.loanapp.common.enums.LoanType;
import com.loanapp.loan.entity.Loan;
import com.loanapp.loan.exception.LoanException;

@Component
public class LoanEligibilityRules {

    public boolean validateLoanLimits(Loan loan,LoanType type, double amount, int tenure) {
        LoanTypeConfig cfg = LoanTypeConfig.from(type);

        if (amount < cfg.getMinAmount() || amount > cfg.getMaxAmount())
        {    
        	loan.setRejectionReason(LoanRejectionReason.AMOUNT_OUT_OF_RANGE);
        	return true;
        }

        if (tenure < cfg.getMinTenure() || tenure > cfg.getMaxTenure())
        {
        	loan.setRejectionReason(LoanRejectionReason.TENURE_OUT_OF_RANGE);
        	return true;
        }
        
        return false;
           
    }

    public boolean validatePerson(Loan loan, int creditScore, double income, int age) {
    	
        if (creditScore < 600) {
        	loan.setRejectionReason(LoanRejectionReason.LOW_CREDIT_SCORE);
        	return true;
        }
        if (income < 15000) {
        	loan.setRejectionReason(LoanRejectionReason.INSUFFICIENT_INCOME);
        	return true;
        }
        if (age < 21 || age > 65) {
        	loan.setRejectionReason(LoanRejectionReason.AGE_NOT_ELIGIBLE);
        	return true;
        
        }
        return false;
    }
}
