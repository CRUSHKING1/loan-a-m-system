package com.loanapp.emi.entity;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.loanapp.common.enums.EmiStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "emis")
@Data
public class Emi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long loanId;
    private Integer emiNumber;

    private BigDecimal emiAmount;
    private BigDecimal penaltyAmount;
    private BigDecimal totalPayable;

    private LocalDate dueDate;
    private LocalDate paidDate;

    @Enumerated(EnumType.STRING)
    private EmiStatus status;

    // getters & setters
}