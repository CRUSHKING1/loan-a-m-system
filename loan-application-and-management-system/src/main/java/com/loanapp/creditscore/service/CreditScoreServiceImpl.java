package com.loanapp.creditscore.service;

import com.loanapp.creditscore.dto.CreditScoreRequestDto;
import com.loanapp.creditscore.dto.CreditScoreResponseDto;
import com.loanapp.creditscore.entity.CreditScore;
import com.loanapp.creditscore.repository.CreditScoreRepository;
import com.loanapp.creditscore.service.CreditScoreService;
import com.loanapp.creditscore.util.CreditScoreConstants;
import org.springframework.stereotype.Service;

@Service
public class CreditScoreServiceImpl implements CreditScoreService {

    private final CreditScoreRepository repository;
 

   

    public CreditScoreServiceImpl(CreditScoreRepository repository) {
		super();
		this.repository = repository;
		
	}



	@Override
    public CreditScoreResponseDto getOrCreateCreditScore(String panNumber) {
        
    	
        CreditScore creditScore = repository.findByPanNumber(panNumber)
                .orElseGet(() -> {
                    CreditScore cs = new CreditScore();
                    cs.setPanNumber(panNumber);
                    cs.setScore(CreditScoreConstants.DEFAULT_CREDIT_SCORE);
                    return repository.save(cs);
                });

        return new CreditScoreResponseDto(
                creditScore.getPanNumber(),
                creditScore.getScore()
        );
    }
}