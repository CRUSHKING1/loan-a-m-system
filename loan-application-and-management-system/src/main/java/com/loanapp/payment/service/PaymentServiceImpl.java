package com.loanapp.payment.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanapp.common.enums.EmiStatus;
import com.loanapp.common.enums.LoanStatus;
import com.loanapp.common.enums.PaymentStatus;
import com.loanapp.config.security.useridgetter.SecurityUtils;
import com.loanapp.emi.entity.Emi;
import com.loanapp.emi.repository.EmiRepository;
import com.loanapp.emi.util.PenaltyCalculator;
import com.loanapp.loan.entity.Loan;
import com.loanapp.loan.repository.LoanRepository;
import com.loanapp.payment.dto.PaymentResponseDto;
import com.loanapp.payment.entity.Payment;
import com.loanapp.payment.repository.PaymentRepository;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final EmiRepository emiRepository;
    private final PaymentRepository paymentRepository;
    private final LoanRepository loanRepository;

    public PaymentServiceImpl(
            EmiRepository emiRepository,
            PaymentRepository paymentRepository,
            LoanRepository loanRepository) {

        this.emiRepository = emiRepository;
        this.paymentRepository = paymentRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public PaymentResponseDto payEmi(Long emiId, BigDecimal amount) {

        Long userId = SecurityUtils.getCurrentUserId(); // ✅ FIX

        Emi emi = emiRepository.findById(emiId)
                .orElseThrow(() -> new RuntimeException("EMI not found"));

        validateEmiOwnership(emi, userId); // ✅ BOLA FIX

        if (emi.getStatus() == EmiStatus.PAID) {
            throw new RuntimeException("EMI already paid");
        }

        BigDecimal penalty = BigDecimal.ZERO;

        if (LocalDate.now().isAfter(emi.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(
                    emi.getDueDate(), LocalDate.now());
            penalty = PenaltyCalculator.calculate(
                    emi.getEmiAmount(), daysLate);
            emi.setStatus(EmiStatus.OVERDUE);
        }

        BigDecimal totalPayable = emi.getEmiAmount().add(penalty);

        if (amount.compareTo(totalPayable) != 0) {
            throw new RuntimeException(
                    "Exact EMI amount required: " + totalPayable);
        }

        // Update EMI
        emi.setPenaltyAmount(penalty);
        emi.setTotalPayable(totalPayable);
        emi.setPaidDate(LocalDate.now());
        emi.setStatus(EmiStatus.PAID);
        emiRepository.save(emi);

        // Save Payment
        Payment payment = new Payment();
        payment.setEmiId(emiId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        closeLoanIfCompleted(emi.getLoanId());

        // Response DTO
        PaymentResponseDto response = new PaymentResponseDto();
        response.setPaymentId(payment.getId());
        response.setEmiId(emiId);
        response.setAmount(amount);
        response.setStatus(PaymentStatus.SUCCESS);
        response.setPaymentDate(payment.getPaymentDate());
        response.setMessage("EMI payment successful");

        return response;
    }

    // 🔐 BOLA VALIDATION (NEW)
    private void validateEmiOwnership(Emi emi, Long userId) {

        Loan loan = loanRepository.findById(emi.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (!loan.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized EMI payment attempt");
        }
    }

    private void closeLoanIfCompleted(Long loanId) {

        long pending = emiRepository.countByLoanIdAndStatus(
                loanId, EmiStatus.PENDING);

        long overdue = emiRepository.countByLoanIdAndStatus(
                loanId, EmiStatus.OVERDUE);

        if (pending == 0 && overdue == 0) {
            Loan loan = loanRepository.findById(loanId).get();
            loan.setStatus(LoanStatus.COMPLETED);
            loanRepository.save(loan);
        }
    }
}