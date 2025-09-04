package com.vinay.wipro.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vinay.wipro.entity.Account;
import com.vinay.wipro.entity.Customer;
import com.vinay.wipro.entity.KycStatus;
import com.vinay.wipro.feign.AccountFeign;
import com.vinay.wipro.repo.Accountrepo;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final Accountrepo accountrepo;

    private final AccountFeign accountFeign;
    
	@Override
	public Account getAccountById(Long id) {
		Account existingAccount=accountrepo.findById(id).orElseThrow(
				()->new RuntimeException("account not found with id"));
		
		return existingAccount;
	}

	@Override
	public Long getAccountBalance(Long id) {
		Account existingAccount=accountrepo.findById(id).orElseThrow(
				()->new RuntimeException("account not found with id"));
		return existingAccount.getBalance();
	}

	
	@Override
	public Account createAccount(@Valid Account account) {
	    Customer customer = accountFeign.getCustomerByName(account.getName());

	    if (customer.getKyc() == KycStatus.VERIFIED) {
	        
	        account.setCustomerId(customer.getId());
	        return accountrepo.save(account);
	    } else {
	        throw new RuntimeException("Customer KYC not verified. Cannot create account.");
	    }
	}

	@Override
    public Account getAccountByNumber(String accountNumber) {
        return accountrepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found with number: " + accountNumber));
    }

	public void updateAccountBalance(String accountNumber, Long newBalance) {
	    Account account = accountrepo.findByAccountNumber(accountNumber)
	                        .orElseThrow(() -> new RuntimeException("Account not found"));
	    account.setBalance(newBalance);
	    accountrepo.save(account);
	}

	@Override
	public List<Account> getAllAccounts() {
		
		return accountrepo.findAll();
	}

	@Override
	public Long getCustomerId(String accountNumber) {
		Account account = accountrepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
		return account.getCustomerId();
	}

	@Override
	public Account getAccountByCustomerId(Long id) {
		Account account = accountrepo.findBycustomerId(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
		return account;
		
	}

	


	
}
