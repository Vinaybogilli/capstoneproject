package com.vinay.wipro.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "audit_logs")
public class AuditDTO {
    private String fromAccount;
    private String toAccount;
    private Long amount;
    private String status;
    private boolean external;
}
