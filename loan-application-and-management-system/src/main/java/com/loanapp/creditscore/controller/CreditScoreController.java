package com.loanapp.creditscore.controller;

import com.loanapp.creditscore.dto.CreditScoreRequestDto;
import com.loanapp.creditscore.dto.CreditScoreResponseDto;
import com.loanapp.creditscore.service.CreditScoreService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credit-score")
public class CreditScoreController {

    private final CreditScoreService service;

    public CreditScoreController(CreditScoreService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CreditScoreResponseDto> getCreditScore(
            @Valid @RequestBody CreditScoreRequestDto request) {

        return ResponseEntity.ok(
                service.getOrCreateCreditScore(request.getPanNumber())
        );
    }
}



