package com.loanapp.kyc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loanapp.kyc.dto.KycResponseDto;
import com.loanapp.kyc.service.KycService;

@RestController
@RequestMapping("/api/admin/kyc")
public class KycAdminController {

    private final KycService kycService;

    public KycAdminController(KycService kycService) {
        this.kycService = kycService;
    }

    // View all pending KYCs
    @GetMapping("/pending")
    public ResponseEntity<List<KycResponseDto>> getPendingKycs() {
        return ResponseEntity.ok(kycService.getPendingKycs());
    }

    // Approve KYC
    @PutMapping("/{userId}/approve")
    public ResponseEntity<KycResponseDto> approve(@PathVariable Long userId) {
        return ResponseEntity.ok(kycService.approveKyc(userId));
    }

    // Reject KYC
    @PutMapping("/{userId}/reject")
    public ResponseEntity<KycResponseDto> reject(@PathVariable Long userId) {
        return ResponseEntity.ok(kycService.rejectKyc(userId));
    }
}