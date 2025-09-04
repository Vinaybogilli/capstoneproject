package com.vinay.wipro.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.vinay.wipro.entities.AuditDTO;

@FeignClient(name = "AuditService")
public interface AuditFeign {

    @PostMapping("/api/audit/log")
    void logTransaction(AuditDTO audit);
}
