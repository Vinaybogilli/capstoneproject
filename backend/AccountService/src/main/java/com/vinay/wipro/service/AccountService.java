package com.vinay.wipro.service;

import java.util.List;

import com.vinay.wipro.entity.Account;

import jakarta.validation.Valid;

public interface AccountService {

	Account createAccount(@Valid Account account);

	Account getAccountById(Long id);

    Long getAccountBalance(Long id);

	Account getAccountByNumber(String accountNumber);

	void updateAccountBalance(String accountNumber, Long balance);

	List<Account> getAllAccounts();

	Long getCustomerId(String accountNumber);

	

	Account getAccountByCustomerId(Long id);

}
