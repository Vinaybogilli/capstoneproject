package com.vinay.wipro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Random;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message="name should not be blank")
    private String Name;

    @Column(unique = true, length = 15)
    private String accountNumber;

    @NotBlank(message = "username not null")
    private String userName;

    @NotBlank(message = "pin not be null")
    @Size(min = 6, max = 8, message = "pin must be 6 to 8 length")
    private String pin;

    
    private Long balance = 500L;

    private Long loan = 0L;
    
    @NotNull(message="Account Type Can't be null")
    @Enumerated(EnumType.STRING)
    private AccountTypes accountType;

    @PrePersist
    public void generateAccountNumber() {
        if (this.accountNumber == null) {
            this.accountNumber = generateUniqueAccountNumber();
        }
    }

    private String generateUniqueAccountNumber() {
        String bankCode = "2025";
        Random random = new Random();
        long randomPart = 10000000L + random.nextInt(90000000);
        return bankCode + randomPart;
    }
    
    private Long customerId;
}

