package com.loanapp.emi.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loanapp.common.enums.EmiStatus;
import com.loanapp.emi.entity.Emi;
import com.loanapp.emi.service.EmiService;

@RestController
@RequestMapping("/admin/emis")
@PreAuthorize("hasRole('ADMIN')")
public class AdminEmiController {

    private final EmiService emiService;

    public AdminEmiController(EmiService emiService) {
        this.emiService = emiService;
    }

    @GetMapping("/all")
    public List<Emi> allEmis() {
        return emiService.getAllEmisAdmin();
    }

    @GetMapping("/status/{status}")
    public List<Emi> byStatus(@PathVariable EmiStatus status) {
        return emiService.getEmisByStatusAdmin(status);
    }

    @GetMapping("/loan/{loanId}")
    public List<Emi> byLoanId(@PathVariable Long loanId) {
        return emiService.getEmisByLoanIdAdmin(loanId);
    }
}
