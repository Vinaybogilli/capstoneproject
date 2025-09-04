package com.vinay.wipro.service;

import java.util.List;

import com.vinay.wipro.entities.Customer;

import jakarta.validation.Valid;

public interface CustomerService {

	Customer createCustomer(@Valid Customer customer);

	List<Customer> getAllCustomers();

	Customer getCustomerByName(String name);

	Customer getCustomerById(Long id);
	
	String  getCustomerEmailById(Long id);

	Customer getCustomerByEmail(String email);

	String getCustomerEmail(Long id);
	

}
