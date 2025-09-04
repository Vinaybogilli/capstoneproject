package com.vinay.wipro.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sender account will be fetched from AccountService via Feign
    @Column(length = 15)
    private String fromAccountNumber;

    @NotBlank(message = "Receiver account number is required")
    @Column(nullable = false, length = 15)
    private String toAccountNumber;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than 0")
    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private boolean isExternal; // true if NEFT/RTGS external transfer

    private LocalDateTime transactionTime;

    @PrePersist
    public void setTransactionTime() {
        this.transactionTime = LocalDateTime.now();
        if (this.status == null) {
            this.status = PaymentStatus.PENDING;
        }
    }

}
