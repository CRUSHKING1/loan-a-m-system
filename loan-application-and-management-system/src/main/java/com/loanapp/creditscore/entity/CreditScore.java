package com.loanapp.creditscore.entity;

import com.loanapp.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "credit_score",
    uniqueConstraints = @UniqueConstraint(columnNames = "pan_number")
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CreditScore extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pan_number", nullable = false, length = 10)
    private String panNumber;

    @Column(nullable = false)
    private Integer score;

    
}