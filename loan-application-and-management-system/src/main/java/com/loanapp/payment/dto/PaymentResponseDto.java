package com.loanapp.payment.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.loanapp.common.enums.PaymentStatus;

import lombok.Data;


@Data
public class PaymentResponseDto {

    private Long paymentId;
    private Long emiId;
    private Long userId;

    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

    private String message;

}