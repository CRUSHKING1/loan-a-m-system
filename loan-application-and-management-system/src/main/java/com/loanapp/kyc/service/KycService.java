package com.loanapp.kyc.service;

import com.loanapp.kyc.dto.KycRequestDto;
import com.loanapp.kyc.dto.KycResponseDto;
import com.loanapp.kyc.exception.KycNotFoundException;

import java.util.List;

public interface KycService {

    KycResponseDto submitKyc(Long userId, KycRequestDto dto);

    KycResponseDto getLatestKycForUser(Long userId) throws KycNotFoundException;

    boolean isKycApproved(Long userId);

    List<KycResponseDto> getPendingKycs();

    KycResponseDto approveKyc(Long kycId);

    KycResponseDto rejectKyc(Long kycId);
    
}
