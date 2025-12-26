package com.loanapp.emi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.loanapp.common.enums.EmiStatus;

import lombok.Data;

@Data
public class EmiResponseDto {

    private Long emiId;
    private Integer emiNumber;
    private BigDecimal emiAmount;
    private BigDecimal penaltyAmount;
    private BigDecimal totalPayable;
    private LocalDate dueDate;
    private EmiStatus status;

   
}