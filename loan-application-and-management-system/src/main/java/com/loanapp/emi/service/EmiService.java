package com.loanapp.emi.service;

import java.util.List;

import com.loanapp.common.enums.EmiStatus;
import com.loanapp.emi.dto.EmiResponseDto;
import com.loanapp.emi.entity.Emi;
import com.loanapp.loan.entity.Loan;

public interface EmiService {

    // ===== INTERNAL / SYSTEM =====
    void generateEmiSchedule(Loan loan);

    // ===== USER APIs (BOLA-PROTECTED) =====
    List<EmiResponseDto> getAllEmis(Long loanId);
    List<EmiResponseDto> getUpcomingEmis(Long loanId);

    // ===== ADMIN APIs (ROLE-BASED) =====
    List<Emi> getAllEmisAdmin();
    List<Emi> getEmisByStatusAdmin(EmiStatus status);
    List<Emi> getEmisByLoanIdAdmin(Long loanId);
}
