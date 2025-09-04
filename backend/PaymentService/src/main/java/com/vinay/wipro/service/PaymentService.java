package com.vinay.wipro.service;

import com.vinay.wipro.entities.Payment;

public interface PaymentService {

	
    Payment processInternalPayment(Payment payment);

   
    Payment processExternalPayment(Payment payment);

}
