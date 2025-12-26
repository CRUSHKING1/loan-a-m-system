package com.loanapp.emi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loanapp.emi.entity.Emi;
import com.loanapp.emi.repository.EmiRepository;

@RestController
@RequestMapping("/admin/emis")
public class AdminEmiController {

    private final EmiRepository emiRepository;

    public AdminEmiController(EmiRepository emiRepository) {
        this.emiRepository = emiRepository;
    }

    @GetMapping("/all")
    public List<Emi> allEmis() {
        return emiRepository.findAll();
    }
}