package com.loanapp.kyc.dto;

import com.loanapp.common.enums.KycStatus;

import lombok.Data;
@Data
public class KycResponseDto {

    private Long kycId;
    private Long userId;
    private String fullName;
    private String panNumber;
    private String aadhaarLast4;
    private Double monthlyIncome;
    private String employmentStatus;
    private KycStatus status;
}
