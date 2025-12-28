package com.loanapp.emi.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.loanapp.common.enums.EmiStatus;
import com.loanapp.emi.dto.EmiResponseDto;
import com.loanapp.emi.entity.Emi;
import com.loanapp.emi.repository.EmiRepository;
import com.loanapp.emi.util.EmiEligibilityRule;
import com.loanapp.loan.entity.Loan;

@Service
public class EmiServiceImpl implements EmiService {

    private final EmiRepository emiRepository;

    public EmiServiceImpl(EmiRepository emiRepository) {
        this.emiRepository = emiRepository;
    }

    @Override
    public void generateEmiSchedule(Loan loan, double monthlyIncome) {

        BigDecimal emiAmount = BigDecimal.valueOf(loan.getEmiAmount());
        EmiEligibilityRule.validate(monthlyIncome, emiAmount);

        for (int i = 1; i <= loan.getTenureMonths(); i++) {

            Emi emi = new Emi();
            emi.setLoanId(loan.getId());
            emi.setEmiNumber(i);
            emi.setEmiAmount(emiAmount);
            emi.setPenaltyAmount(BigDecimal.ZERO);
            emi.setTotalPayable(emiAmount);
            emi.setDueDate(LocalDate.now().plusMonths(i));
            emi.setStatus(EmiStatus.PENDING);

            emiRepository.save(emi);
        }
    }

    @Override
    public List<EmiResponseDto> getAllEmis(Long loanId) {
    	
    	
        return emiRepository.findByLoanId(loanId)
                .stream().map(this::map).toList();
    }

    @Override
    public List<EmiResponseDto> getUpcomingEmis(Long loanId) {
        return emiRepository
                .findByLoanIdAndDueDateAfterAndStatus(
                        loanId, LocalDate.now(), EmiStatus.PENDING)
                .stream().map(this::map).toList();
    }

    private EmiResponseDto map(Emi emi) {
        EmiResponseDto dto = new EmiResponseDto();
        dto.setEmiId(emi.getId());
        dto.setEmiNumber(emi.getEmiNumber());
        dto.setEmiAmount(emi.getEmiAmount());
        dto.setPenaltyAmount(emi.getPenaltyAmount());
        dto.setTotalPayable(emi.getTotalPayable());
        dto.setDueDate(emi.getDueDate());
        dto.setStatus(emi.getStatus());
        return dto;
    }
}