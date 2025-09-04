package com.vinay.wipro.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;
    private String name;
    private String accountNumber;
    private String userName;
    private Long balance;
    private Long loan;
    private String accountType;
    private Long customerId;
}
