package com.vinay.wipro.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "AccountService")
public interface AccountFeign {

    @GetMapping("/api/bank/{accountNumber}")
    AccountResponse getAccountByNumber(@PathVariable String accountNumber);

    @PutMapping("/api/bank/updatebalance/{accountNumber}/{balance}")
    void updateAccountBalance(@PathVariable String accountNumber, @PathVariable Long balance);

    @GetMapping("/api/bank/getCustomerId/{accountNumber}")
    Long getCustomerId(@PathVariable String accountNumber);

    class AccountResponse {
        private String accountNumber;
        private String accountHolderName;
        private Long balance;

        public String getAccountNumber() { return accountNumber; }
        public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

        public String getAccountHolderName() { return accountHolderName; }
        public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

        public Long getBalance() { return balance; }
        public void setBalance(Long balance) { this.balance = balance; }
    }
}
