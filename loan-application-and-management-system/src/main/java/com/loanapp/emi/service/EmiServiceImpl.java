package com.loanapp.emi.service;

import java.math.BigDecimal;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.loanapp.common.enums.EmiStatus;
import com.loanapp.config.security.useridgetter.SecurityUtils;
import com.loanapp.emi.dto.EmiResponseDto;
import com.loanapp.emi.entity.Emi;
import com.loanapp.emi.exception.EmiException;
import com.loanapp.emi.repository.EmiRepository;
import com.loanapp.emi.util.EmiEligibilityRule;
import com.loanapp.loan.entity.Loan;
import com.loanapp.loan.repository.LoanRepository;

@Service
public class EmiServiceImpl implements EmiService {

    private final EmiRepository emiRepository;
    private final LoanRepository loanRepository;

    public EmiServiceImpl(EmiRepository emiRepository,
                          LoanRepository loanRepository) {
        this.emiRepository = emiRepository;
        this.loanRepository = loanRepository;
    }

    // ================= SYSTEM =================

    @Override
    public void generateEmiSchedule(Loan loan) {

        BigDecimal emiAmount = BigDecimal.valueOf(loan.getEmiAmount());
     

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

    // ================= USER APIs (BOLA SAFE) =================

    @Override
    public List<EmiResponseDto> getAllEmis(Long loanId) {
        validateLoanOwnership(loanId);
        return emiRepository.findByLoanId(loanId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<EmiResponseDto> getUpcomingEmis(Long loanId) {
        validateLoanOwnership(loanId);
        return emiRepository
                .findByLoanIdAndDueDateAfterAndStatus(
                        loanId, LocalDate.now(), EmiStatus.PENDING)
                .stream()
                .map(this::map)
                .toList();
    }

    // ================= ADMIN APIs =================

    @Override
    public List<Emi> getAllEmisAdmin() {
        return emiRepository.findAll();
    }

    @Override
    public List<Emi> getEmisByStatusAdmin(EmiStatus status) {
        return emiRepository.findAll()
                .stream()
                .filter(emi -> emi.getStatus() == status)
                .toList();
    }

    @Override
    public List<Emi> getEmisByLoanIdAdmin(Long loanId) {
        return emiRepository.findByLoanId(loanId);
    }

    // ================= INTERNAL =================

    private void validateLoanOwnership(Long loanId) {
        Long userId = getAuthenticatedUserId();

        loanRepository.findById(loanId)
                .filter(loan -> loan.getUserId().equals(userId))
                .orElseThrow(() ->
                        new EmiException("Unauthorized access to loan EMIs"));
    }

    private Long getAuthenticatedUserId() {
        return SecurityUtils.getCurrentUserId();
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
