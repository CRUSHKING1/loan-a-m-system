package com.loanapp.kyc.util;

import com.loanapp.common.enums.EmploymentStatus;
import com.loanapp.kyc.dto.KycRequestDto;
import com.loanapp.kyc.dto.KycResponseDto;
import com.loanapp.kyc.entity.Kyc;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class KycMapper {

    // Convert KycRequestDto to Kyc entity
    public Kyc toEntity(KycRequestDto requestDto) {
        Kyc kyc = new Kyc();
        kyc.setFullName(requestDto.getFullName());
        kyc.setPanNumber(requestDto.getPanNumber());
        kyc.setAadhaarLast4(requestDto.getAadhaarLast4());
        kyc.setMonthlyIncome(requestDto.getMonthlyIncome());
        kyc.setDateOfBirth(requestDto.getDateOfBirth());
        // Convert employmentStatus from String to enum
        if (requestDto.getEmploymentStatus() != null) {
            kyc.setEmploymentStatus(EmploymentStatus.valueOf(requestDto.getEmploymentStatus()));
        }
//       
//        // Convert dateOfBirth from String to LocalDate
//        if (requestDto.getDateOfBirth() != null) {
//            kyc.setDateOfBirth(LocalDate.parse(requestDto.getDateOfBirth())); // Assumes ISO format (yyyy-MM-dd)
//        }

        return kyc;
    }

    // Convert Kyc entity to KycResponseDto
    public KycResponseDto toResponseDto(Kyc kyc) {
        KycResponseDto responseDto = new KycResponseDto();
        responseDto.setUserId(kyc.getUserId());
        responseDto.setFullName(kyc.getFullName());
        responseDto.setPanNumber(kyc.getPanNumber());
        responseDto.setAadhaarLast4(kyc.getAadhaarLast4());
        responseDto.setStatus(kyc.getStatus());
        responseDto.setMonthlyIncome(kyc.getMonthlyIncome());
        responseDto.setEmploymentStatus(kyc.getEmploymentStatus().name());

        // Calculate and set the age dynamically
        responseDto.setAge(kyc.getAge());

        return responseDto;
    }

    // Convert KycResponseDto to Kyc entity
    public Kyc toEntityFromResponseDto(KycResponseDto responseDto) {
        Kyc kyc = new Kyc();
        kyc.setUserId(responseDto.getUserId());
        kyc.setFullName(responseDto.getFullName());
        kyc.setPanNumber(responseDto.getPanNumber());
        kyc.setAadhaarLast4(responseDto.getAadhaarLast4());
        kyc.setStatus(responseDto.getStatus());

        // Assume other fields like monthlyIncome and employmentStatus are already set if needed

        return kyc;
    }
}
