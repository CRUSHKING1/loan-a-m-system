package com.loanapp.loan.service;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loanapp.common.enums.LoanRejectionReason;
import com.loanapp.common.enums.LoanStatus;
import com.loanapp.common.enums.LoanType;
import com.loanapp.config.security.useridgetter.SecurityUtils;
import com.loanapp.creditscore.service.CreditScoreService;
import com.loanapp.emi.service.EmiService;
import com.loanapp.emi.util.EmiEligibilityRule;
import com.loanapp.kyc.entity.Kyc;
import com.loanapp.kyc.exception.KycNotFoundException;
import com.loanapp.kyc.repository.KycRepository;
import com.loanapp.kyc.service.KycService;
import com.loanapp.loan.dto.LoanApplyRequestDto;
import com.loanapp.loan.dto.LoanApprovalRequestDto;
import com.loanapp.loan.dto.LoanPreviewResponseDto;
import com.loanapp.loan.dto.LoanResponseDto;
import com.loanapp.loan.entity.Loan;
import com.loanapp.loan.exception.LoanException;
import com.loanapp.loan.repository.LoanRepository;
import com.loanapp.loan.rule.InterestRuleEngine;
import com.loanapp.loan.rule.LoanEligibilityRules;
import com.loanapp.loan.util.EmiCalculator;
import com.loanapp.user.exception.UserNotFoundException;
import com.loanapp.user.repository.UserRepository;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanEligibilityRules eligibilityRules;
    private final InterestRuleEngine interestRuleEngine;
    private final KycRepository kycRepository;
    private final EmiService emiService;
    private final KycService kycService;
    private final UserRepository userRepository;
    public final SecurityUtils securityutils;
    private final CreditScoreService creditScoreService;

    public LoanServiceImpl(
            LoanRepository loanRepository,
            LoanEligibilityRules eligibilityRules,
            InterestRuleEngine interestRuleEngine,
            KycRepository kycRepository,
            EmiService emiService,
            KycService kycService,
            UserRepository userRepository,
            SecurityUtils securityutils,
            CreditScoreService creditScoreService) {

        this.loanRepository = loanRepository;
        this.eligibilityRules = eligibilityRules;
        this.interestRuleEngine = interestRuleEngine;
        this.kycRepository = kycRepository;
        this.emiService = emiService;
        this.kycService = kycService;
        this.userRepository = userRepository;
        this.securityutils=securityutils;
       
        this.creditScoreService = creditScoreService;
    }

    // ===================== USER APIs =====================

    @Override
    public LoanResponseDto applyLoan(LoanApplyRequestDto request)
            throws UserNotFoundException, KycNotFoundException {

        Long userId = SecurityUtils.getCurrentUserId();
        
        userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        Kyc kyc = kycRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new KycNotFoundException("No KYC found"));

        int creditScore = creditScoreService.getOrCreateCreditScore1(kyc.getPanNumber());

        double interestRate = interestRuleEngine.calculate(
                request.getLoanType(),
                creditScore,
                kyc.getMonthlyIncome(),
                kyc.getAge(),
                request.getTenureMonths()
        );

        double emiAmount = EmiCalculator.calculate(
                request.getLoanAmount(),
                interestRate,
                request.getTenureMonths()
        );
  
        
        Loan loan = new Loan();
        loan.setUserId(userId);
        loan.setLoanType(request.getLoanType());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setInterestRate(interestRate);
        loan.setEmiAmount(emiAmount);
        
        
       
        LoanType requestedType = request.getLoanType();
        
      if( eligibilityRules.validatePerson(loan, creditScore, kyc.getMonthlyIncome(), kyc.getAge()))
      {
    	  loan.setStatus(LoanStatus.REJECTED);
    	
    	   return mapToDto(loanRepository.save(loan));
      }
        
        
        
        
        
        if (!kycService.isKycApproved(userId)) {
           loan.setRejectionReason(LoanRejectionReason.KYC_INCOMPLETE); 
           loan.setStatus(LoanStatus.REJECTED);
               return mapToDto(loanRepository.save(loan));
              
        }
     
        long activeLoanCount =
                loanRepository.countByUserIdAndStatus(userId, LoanStatus.ACTIVE);
             loan.setStatus(LoanStatus.REJECTED);

        if (activeLoanCount >= 3) {
        	
        	loan.setRejectionReason(LoanRejectionReason.LOAN_COUNT_EXCEEDS);
        	loan.setStatus(LoanStatus.REJECTED);
        	
        	return mapToDto(loanRepository.save(loan));
            
        }

        boolean sameTypeActive =
                loanRepository.existsByUserIdAndLoanTypeAndStatus(
                        userId, requestedType, LoanStatus.ACTIVE);

        if (sameTypeActive && requestedType != LoanType.PERSONAL) {
            loan.setRejectionReason(LoanRejectionReason.ACTIVE_LOAN_EXISTS);
            loan.setStatus(LoanStatus.REJECTED);
            loanRepository.save(loanRepository.save(loan));
            return mapToDto(loan);
        }


       if( eligibilityRules.validateLoanLimits(
        		loan,
                request.getLoanType(),
                request.getLoanAmount(),
                request.getTenureMonths()
        )) {
    	   loan.setStatus(LoanStatus.REJECTED);
    	
     	  return mapToDto(loanRepository.save(loan));
       }


      
       
        
        BigDecimal emiAmountBigDecimal = new BigDecimal(String.valueOf(emiAmount));

     // Now pass the BigDecimal to the validate method
     

        boolean eligible = EmiEligibilityRule.validate(kyc.getMonthlyIncome(), emiAmountBigDecimal);

        if (eligible) {
            loan.setStatus(LoanStatus.ACTIVE);
          
            loan = loanRepository.save(loan);
            emiService.generateEmiSchedule(loan);
        } else {
            loan.setStatus(LoanStatus.REJECTED);
            loan.setRejectionReason(LoanRejectionReason.EMI_AMOUNT_NOT_IN_RANGE);
           
            return mapToDto(loanRepository.save(loan));
        }
        loan.setDisbursedDate(LocalDate.now());      
        return mapToDto(loan);
    }

    

	@Override
    public LoanPreviewResponseDto previewLoan(LoanApplyRequestDto request)
            throws UserNotFoundException, KycNotFoundException {

        Long userId = SecurityUtils.getCurrentUserId();

        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!kycService.isKycApproved(userId)) {
            throw new LoanException("KYC is not approved");
        }

        Kyc kyc = kycRepository.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new KycNotFoundException("No KYC found"));

        int creditScore = creditScoreService.getOrCreateCreditScore1(kyc.getPanNumber());

        double interestRate = interestRuleEngine.calculate(
                request.getLoanType(),
                creditScore,
                kyc.getMonthlyIncome(),
                kyc.getAge(),
                request.getTenureMonths()
        );

        double emiAmount = EmiCalculator.calculate(
                request.getLoanAmount(),
                interestRate,
                request.getTenureMonths()
        );

        LoanPreviewResponseDto dto = new LoanPreviewResponseDto();
        dto.setInterestRate(interestRate);
        dto.setEmiAmount(emiAmount);
        dto.setTotalPayable(emiAmount * request.getTenureMonths());
     
        return dto;
    }

    @Override
    public List<LoanResponseDto> getMyLoans()
            throws UserNotFoundException {

        Long userId = SecurityUtils.getCurrentUserId();

        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return loanRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<LoanResponseDto> getMyLoansByStatus(LoanStatus status)
            throws UserNotFoundException {

        Long userId = SecurityUtils.getCurrentUserId();

        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return loanRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    

    // ===================== ADMIN APIs =====================

    @Override
    public LoanResponseDto approveOrRejectLoan(Long loanId, LoanApprovalRequestDto request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanException("Loan not found"));

        if (request.isApproved()) {
            loan.setStatus(LoanStatus.APPROVED);
            loan.setDisbursedDate(LocalDate.now());
            loan.setRejectionReason(null);
            emiService.generateEmiSchedule(loan);
            //emi gen
        } else {
            loan.setStatus(LoanStatus.REJECTED);
            loan.setRejectionReason(request.getRejectionReason());
        }

        
        return mapToDto(loanRepository.save(loan));
    }

    @Override
    public LoanResponseDto activateLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanException("Loan not found"));

        loan.setStatus(LoanStatus.ACTIVE);
        loanRepository.save(loan);

        return mapToDto(loan);
    }

    @Override
    public List<LoanResponseDto> getAllLoans() {
        return loanRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
       }
    @Override
    public List<LoanResponseDto> getLoansByStatus(LoanStatus status) {
        return loanRepository.findByStatus(status)
                .stream()
                .map(this::mapToDto)
                .toList();
    }
    private LoanResponseDto mapToDto(Loan loan) {
        LoanResponseDto dto = new LoanResponseDto();
        dto.setLoanId(loan.getId());
        dto.setLoanType(loan.getLoanType());
        dto.setLoanAmount(loan.getLoanAmount());
        dto.setTenureMonths(loan.getTenureMonths());
        dto.setInterestRate(loan.getInterestRate());
        dto.setEmiAmount(loan.getEmiAmount());
        dto.setStatus(loan.getStatus());
        dto.setRejectionReason(loan.getRejectionReason());
        return dto;
    }
}
                
