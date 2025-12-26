package com.loanapp.creditscore.service;


import com.loanapp.creditscore.dto.CreditScoreResponseDto;

public interface CreditScoreService {

	
	CreditScoreResponseDto getOrCreateCreditScore(String panNumber);
}
