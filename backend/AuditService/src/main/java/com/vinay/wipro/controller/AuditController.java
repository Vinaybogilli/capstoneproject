package com.vinay.wipro.controller;

import com.vinay.wipro.entity.Audit;
import com.vinay.wipro.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @PostMapping("/log")
    public Audit logTransaction(@RequestBody Audit audit) {
        return auditService.saveAudit(audit);
    }
}
