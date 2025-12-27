package com.loanapp.kyc.service;

import java.util.List;

import com.loanapp.kyc.dto.KycRequestDto;
import com.loanapp.kyc.dto.KycResponseDto;
import com.loanapp.kyc.exception.KycNotFoundException;

public interface KycService {

    KycResponseDto submitKyc(Long userId, KycRequestDto dto);

    KycResponseDto approveKyc(Long userId) throws KycNotFoundException;

    KycResponseDto rejectKyc(Long userId) throws KycNotFoundException;

    boolean isKycApproved(Long userId); // used in loan service 
    
    List<KycResponseDto> getPendingKycs();
    
    KycResponseDto getKycByUserId(Long userId) throws KycNotFoundException;
}