package com.fakejobdetector.repository;

import com.fakejobdetector.model.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<JobPosting, String> {

    Page<JobPosting> findAll(Pageable pageable);

    Page<JobPosting> findByRiskLevel(String riskLevel, Pageable pageable);
}
