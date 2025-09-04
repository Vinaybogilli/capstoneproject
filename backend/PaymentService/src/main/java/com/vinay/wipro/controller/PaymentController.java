package com.vinay.wipro.controller;

import com.vinay.wipro.entities.Payment;
import com.vinay.wipro.service.PaymentService;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    
    @PostMapping("/internal")
    public ResponseEntity<String> internalPayment(@RequestBody Payment payment) {
        try {
            paymentService.processInternalPayment(payment);
            return ResponseEntity.ok("Payment successful");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                                 .body("Payment failed: " + e.getMessage());
        }
    }


    
    @PostMapping("/external")
    public Payment externalPayment(@RequestBody Payment payment) {
        return paymentService.processExternalPayment(payment);
    }
}
