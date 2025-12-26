package com.loanapp.creditscore.repository;

import com.loanapp.creditscore.entity.CreditScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditScoreRepository
        extends JpaRepository<CreditScore, Long> {

    Optional<CreditScore> findByPanNumber(String panNumber);
}