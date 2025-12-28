package com.loanapp.kyc.entity;

import java.time.LocalDate;
import java.time.Period;

import com.loanapp.common.audit.Auditable;
import com.loanapp.common.enums.EmploymentStatus;
import com.loanapp.common.enums.KycStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "kyc")
@Getter
@Setter
public class Kyc extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // removed unique for now
    private Long userId;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, length = 10)
    private String panNumber;

    @Column(nullable = false, length = 4)//remoced unique
    private String aadhaarLast4;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KycStatus status;

    @Column(nullable = false)
    private Double monthlyIncome;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentStatus employmentStatus;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    // Calculate age dynamically
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

 
}
