package com.loanapp.loan.entity;

import com.loanapp.common.audit.Auditable;
import com.loanapp.common.enums.LoanRejectionReason;
import com.loanapp.common.enums.LoanStatus;
import com.loanapp.common.enums.LoanType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "loan")
@Getter
@Setter
//** include final emi or last completed date and in emi completion add last emi date so that loan is complete
public class Loan extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    private Double loanAmount;
    private Integer tenureMonths;
    private Double interestRate;
    private Double emiAmount;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Enumerated(EnumType.STRING)
    private LoanRejectionReason rejectionReason;

    private String rejectionRemarks;
    private LocalDate disbursedDate;}
