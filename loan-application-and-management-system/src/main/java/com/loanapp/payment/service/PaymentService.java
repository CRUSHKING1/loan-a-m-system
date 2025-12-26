package com.loanapp.payment.service;

import java.math.BigDecimal;

import com.loanapp.payment.dto.PaymentResponseDto;

public interface PaymentService {

	PaymentResponseDto payEmi(Long emiId, Long userId, BigDecimal amount);
}