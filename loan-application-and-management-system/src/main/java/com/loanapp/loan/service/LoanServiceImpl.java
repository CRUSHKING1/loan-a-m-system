package com.loanapp.loan.service;

import com.loanapp.common.enums.KycStatus;
import com.loanapp.common.enums.LoanStatus;
import com.loanapp.creditscore.entity.CreditScore;
import com.loanapp.creditscore.repository.CreditScoreRepository;
import com.loanapp.emi.service.EmiService;
import com.loanapp.kyc.entity.Kyc;
import com.loanapp.kyc.repository.KycRepository;
import com.loanapp.loan.dto.*;
import com.loanapp.loan.entity.Loan;
import com.loanapp.loan.exception.LoanException;
import com.loanapp.loan.repository.LoanRepository;
import com.loanapp.loan.rule.LoanEligibilityRules;
import com.loanapp.user.entity.User;
import com.loanapp.user.exception.UserNotFoundException;
import com.loanapp.user.repository.UserRepository;
import com.loanapp.loan.rule.InterestRuleEngine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanEligibilityRules eligibilityRules;
    private final InterestRuleEngine interestRuleEngine;
    private final UserRepository userRepository;
    private final KycRepository kycRepository;
    private final CreditScoreRepository creditScoreRepository;
    private final EmiService emiService;



	public LoanServiceImpl(LoanRepository loanRepository, LoanEligibilityRules eligibilityRules,
			InterestRuleEngine interestRuleEngine, UserRepository userRepository, KycRepository kycRepository,
			CreditScoreRepository creditScoreRepository, EmiService emiService) {
		super();
		this.loanRepository = loanRepository;
		this.eligibilityRules = eligibilityRules;
		this.interestRuleEngine = interestRuleEngine;
		this.userRepository = userRepository;
		this.kycRepository = kycRepository;
		this.creditScoreRepository = creditScoreRepository;
		this.emiService = emiService;
	}

	@Override
    public LoanResponseDto applyLoan(Long userId, LoanApplyRequestDto request) {
            //using repo instead of servcie for time restrcition
		   User user = userRepository.findById(userId)      // ** make exception in this servcie
	                .orElseThrow(() -> new UserNotFoundException("User not found"));
		   
		   Kyc kyc=kycRepository.findByUserId(userId).orElseThrow(()->new RuntimeException("KYC NOT FOUND "));// ** make exception in this servcie

		   if(kyc.getStatus()!=KycStatus.APPROVED)throw new RuntimeException("KYC NOT APPROVED Pleaase aprove the kyc");// ** make exception in this servcie

		   
		   
		
		// Step 1: Validate eligibility
        eligibilityRules.validateLoanLimits(request.getLoanType(), request.getLoanAmount(), request.getTenureMonths());
        
      //  CreditScore credScore=creditScoreRepository.findByPanNumber(kyc.getPanNumber()).orElseThrow(()->new RuntimeException("Credit score not found"));
        //** implement credit score service getorsetcreditscore // 0 for credit sxore deafult// update this
        int creditScore=650;

        double income = kyc.getMonthlyIncome();
        int age = kyc.getAge();

        
        
        
        eligibilityRules.validatePerson(creditScore, income, age);

        // Step 2: Calculate interest rate
        double interestRate = interestRuleEngine.calculate(
                request.getLoanType(), creditScore, income, age, request.getTenureMonths());

        // Step 3: Create loan and save
        Loan loan = new Loan();
        loan.setUserId(userId);
        loan.setLoanType(request.getLoanType());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setInterestRate(interestRate);
        loan.setEmiAmount(request.getLoanAmount() * interestRate / 100); // Simplified EMI calculation
        loan.setStatus(LoanStatus.APPLIED);
        
        
       
      
         //** check emi status and affortability and chift it into loan service for proper error handling
        // 🔗 EMI integration
        loan= loanRepository.save(loan);
        emiService.generateEmiSchedule(loan, kyc.getMonthlyIncome());
        loan.setStatus(LoanStatus.ACTIVE);
       
        
        

        return mapToDto(loan);
    }

    @Override
    public LoanPreviewResponseDto previewLoan(Long userId, LoanApplyRequestDto request) {
        // Check loan eligibility (without saving it to DB)
    	   User user = userRepository.findById(userId)      // ** make exception in this servcie
	                .orElseThrow(() -> new UserNotFoundException("User not found"));
		   
		   Kyc kyc=kycRepository.findByUserId(userId).orElseThrow(()->new RuntimeException("KYC NOT FOUND "));// ** make exception in this servcie

		   if(kyc.getStatus()!=KycStatus.APPROVED)throw new RuntimeException("KYC NOT APPROVED Pleaase aprove the kyc");// ** make exception in this servcie

		   
		   
		
		// Step 1: Validate eligibility
       eligibilityRules.validateLoanLimits(request.getLoanType(), request.getLoanAmount(), request.getTenureMonths());
       
       CreditScore credScore=creditScoreRepository.findByPanNumber(kyc.getPanNumber()).orElseThrow(()->new RuntimeException("Credit score not found"));
       
       int creditScore=credScore.getScore();

       double income = kyc.getMonthlyIncome();
       int age = kyc.getAge();

       
        eligibilityRules.validateLoanLimits(request.getLoanType(), request.getLoanAmount(), request.getTenureMonths());
        eligibilityRules.validatePerson(creditScore, income, age);

        // Calculate interest rate based on eligibility
        double interestRate = interestRuleEngine.calculate(request.getLoanType(), creditScore, income, age, request.getTenureMonths());

        // Return the preview information without persisting the loan
        LoanPreviewResponseDto previewDto = new LoanPreviewResponseDto();
        previewDto.setInterestRate(interestRate);
        previewDto.setEmiAmount(request.getLoanAmount() * interestRate / 100);  // Simplified calculation
        previewDto.setTotalPayable(previewDto.getEmiAmount() * request.getTenureMonths());

        return previewDto;
    }

    @Override
    public LoanResponseDto approveOrRejectLoan(Long loanId, LoanApprovalRequestDto request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanException("Loan not found"));

        if (request.isApproved()) {
            loan.setStatus(LoanStatus.APPROVED);
        } else {
            loan.setStatus(LoanStatus.REJECTED);
            loan.setRejectionReason(request.getRejectionReason());
          
        }

        loanRepository.save(loan);
        return mapToDto(loan);
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
    public LoanResponseDto closeLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanException("Loan not found"));
        loan.setStatus(LoanStatus.CLOSED);
        loanRepository.save(loan);
        return mapToDto(loan);
    }

    @Override
    public List<LoanResponseDto> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId).stream()
                .map(this::mapToDto).toList();
    }

    @Override
    public List<LoanResponseDto> getLoansByUserAndStatus(Long userId, LoanStatus status) {
        return loanRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::mapToDto).toList();
    }

    @Override
    public List<LoanResponseDto> getAllLoans() {
        return loanRepository.findAll().stream()
                .map(this::mapToDto).toList();
    }

    @Override
    public List<LoanResponseDto> getLoansByStatus(LoanStatus status) {
        return loanRepository.findByStatus(status).stream()
                .map(this::mapToDto).toList();
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
