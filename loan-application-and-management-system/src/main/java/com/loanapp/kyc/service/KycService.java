package com.loanapp.kyc.service;

import java.util.List;

import com.loanapp.kyc.dto.KycRequestDto;
import com.loanapp.kyc.dto.KycResponseDto;

public interface KycService {

    KycResponseDto submitKyc(Long userId, KycRequestDto dto);

    KycResponseDto approveKyc(Long userId);

    KycResponseDto rejectKyc(Long userId);

    boolean isKycApproved(Long userId); // used in loan service 
    
    List<KycResponseDto> getPendingKycs();
    
    KycResponseDto getKycByUserId(Long userId);
}