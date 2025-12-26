package com.loanapp.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loanapp.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

