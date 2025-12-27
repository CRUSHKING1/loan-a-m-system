package com.loanapp.kyc.service;

import com.loanapp.common.enums.KycStatus;
import com.loanapp.kyc.dto.KycRequestDto;
import com.loanapp.kyc.dto.KycResponseDto;
import com.loanapp.kyc.entity.Kyc;
import com.loanapp.kyc.repository.KycRepository;
import com.loanapp.kyc.util.KycMapper;  // Use KycMapper here

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KycServiceImpl implements KycService {

    private final KycRepository kycRepository;
    private final KycMapper kycMapper;

    public KycServiceImpl(KycRepository kycRepository, KycMapper kycMapper) {
        this.kycRepository = kycRepository;
        this.kycMapper = kycMapper; // Inject the mapper
    }

    @Override
    public KycResponseDto submitKyc(Long userId, KycRequestDto dto) {
        Kyc kyc = kycMapper.toEntity(dto); // Use the mapper to convert DTO to entity
        kyc.setUserId(userId);
        kyc.setStatus(KycStatus.PENDING); // Set status to PENDING by default
        kyc = kycRepository.save(kyc); // Save to the database

        return kycMapper.toResponseDto(kyc); // Convert the entity to DTO and return
    }

    @Override
    public KycResponseDto approveKyc(Long userId) {
        Kyc kyc = kycRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("KYC not found"));

        kyc.setStatus(KycStatus.APPROVED);
        kyc = kycRepository.save(kyc); // Save the updated KYC

        return kycMapper.toResponseDto(kyc); // Convert the entity to DTO and return
    }

    @Override
    public KycResponseDto rejectKyc(Long userId) {
        Kyc kyc = kycRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("KYC not found"));

        kyc.setStatus(KycStatus.REJECTED);
        kyc = kycRepository.save(kyc); // Save the updated KYC

        return kycMapper.toResponseDto(kyc); // Convert the entity to DTO and return
    }

    @Override
    public boolean isKycApproved(Long userId) {
        return kycRepository.findByUserId(userId)
                .map(k -> k.getStatus() == KycStatus.APPROVED)
                .orElse(false);
    }

    @Override
    public List<KycResponseDto> getPendingKycs() {
        return kycRepository.findByStatus(KycStatus.PENDING)
                .stream()
                .map(kycMapper::toResponseDto)
                .toList();
    }

    @Override
    public KycResponseDto getKycByUserId(Long userId) {
        Kyc kyc = kycRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("KYC not found"));
        return kycMapper.toResponseDto(kyc); // Use the mapper to convert entity to DTO
    }
}
