package com.loanapp.loan.repository;



import com.loanapp.common.enums.LoanStatus;
import com.loanapp.common.enums.LoanType;
import com.loanapp.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    boolean existsByUserIdAndLoanTypeAndStatus(
            Long userId, LoanType loanType, LoanStatus status);

    long countByUserIdAndStatus(Long userId, LoanStatus status);

    List<Loan> findByUserId(Long userId);

    List<Loan> findByUserIdAndStatus(Long userId, LoanStatus status);

    List<Loan> findByStatus(LoanStatus status);
    
    

   

}