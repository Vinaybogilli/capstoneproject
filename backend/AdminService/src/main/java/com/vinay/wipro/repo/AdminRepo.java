package com.vinay.wipro.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vinay.wipro.dto.AuditDTO;

public interface AdminRepo extends MongoRepository<AuditDTO, String>{

}
