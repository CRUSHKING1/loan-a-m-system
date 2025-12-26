package com.loanapp.creditscore.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class CreditScoreResponseDto {

    private String panNumber;
    private Integer score;
    
    public CreditScoreResponseDto(String panNumber, Integer score) {
        this.panNumber = panNumber;
        this.score = score;
    }
    
   
}