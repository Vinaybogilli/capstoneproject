package com.vinay.wipro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    private String name;
    private String email;
    private Integer age;
    private AccountType accountType;
    private String phoneNumber;
    private String address;
    private String aadharNumber;
    private String panNumber;
    private KycStatus kyc;
}
