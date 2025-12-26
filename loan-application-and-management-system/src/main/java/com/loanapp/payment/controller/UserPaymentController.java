package com.loanapp.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loanapp.emi.dto.EmiPayRequestDto;
import com.loanapp.payment.dto.PaymentResponseDto;
import com.loanapp.payment.service.PaymentService;


@RestController
@RequestMapping("/user/payments")
public class UserPaymentController {

    private final PaymentService paymentService;

    public UserPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponseDto> pay(@RequestBody EmiPayRequestDto request) {
           
    	System.out.println(request);
        PaymentResponseDto response =
                paymentService.payEmi(
                        request.getEmiId(),
                        request.getUserId(),
                        request.getAmount());

        return ResponseEntity.ok(response);
    }
}