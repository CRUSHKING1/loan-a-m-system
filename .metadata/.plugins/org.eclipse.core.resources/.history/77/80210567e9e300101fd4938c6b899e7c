package com.loanapp.emi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loanapp.emi.dto.EmiResponseDto;
import com.loanapp.emi.service.EmiService;

@RestController
@RequestMapping("/user/emis")
public class UserEmiController {

    private final EmiService emiService;

    public UserEmiController(EmiService emiService) {
        this.emiService = emiService;
    }

    @GetMapping("/{loanId}")
    public List<EmiResponseDto> getAll(@PathVariable Long loanId) {
        return emiService.getAllEmis(loanId);
    }

    @GetMapping("/{loanId}/upcoming")
    public List<EmiResponseDto> upcoming(@PathVariable Long loanId) {
        return emiService.getUpcomingEmis(loanId);
    }
}