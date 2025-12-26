package com.loanapp.payment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loanapp.payment.entity.Payment;
import com.loanapp.payment.repository.PaymentRepository;

@RestController
@RequestMapping("/admin/payments")
public class AdminPaymentController {

    private final PaymentRepository paymentRepository;

    public AdminPaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/all")
    public List<Payment> allPayments() {
        return paymentRepository.findAll();
    }
}
