package com.vinay.wipro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit_logs")
public class Audit {

    @Id
    private String id; 

    private String fromAccount;
    private String toAccount;
    private Double amount;
    private String status;
    private Boolean external;
    private LocalDateTime timestamp;
}
