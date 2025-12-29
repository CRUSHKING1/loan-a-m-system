package com.loanapp.emi.service;

import java.util.List;

import com.loanapp.emi.dto.EmiResponseDto;
import com.loanapp.loan.entity.Loan;

public interface EmiService {

    void generateEmiSchedule(Loan loan);

    List<EmiResponseDto> getAllEmis(Long loanId);

    List<EmiResponseDto> getUpcomingEmis(Long loanId);
}
