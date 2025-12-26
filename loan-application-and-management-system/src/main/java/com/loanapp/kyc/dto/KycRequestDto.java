package com.loanapp.kyc.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import lombok.Data;

@Data
public class KycRequestDto {

    private String fullName;
    private String panNumber;
    private String aadhaarLast4;
    private Double monthlyIncome;
    private String employmentStatus; // Employment status as String
    private LocalDate dateOfBirth; // Date of birth as a String (ISO format yyyy-MM-dd)

}
