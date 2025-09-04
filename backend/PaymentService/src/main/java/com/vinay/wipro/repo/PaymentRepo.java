package com.vinay.wipro.repo;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.vinay.wipro.entities.Payment;

public interface PaymentRepo extends JpaRepositoryImplementation<Payment, Long> {

}
