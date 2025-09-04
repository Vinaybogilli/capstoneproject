package com.vinay.wipro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.vinay.wipro.entities.Customer;

public interface CustomerRepo extends JpaRepositoryImplementation<Customer, Long> {

	Customer findByNameIgnoreCase(String name);

	Optional<Customer> findByEmailIgnoreCase(String email);

}
