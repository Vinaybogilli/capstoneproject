package com.vinay.wipro.service;

import com.vinay.wipro.entity.Audit;
import com.vinay.wipro.repo.AuditRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepo auditRepo;

    public Audit saveAudit(Audit audit) {
        return auditRepo.save(audit);
    }
}
