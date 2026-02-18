package com.fakejobdetector.repository;

import com.fakejobdetector.model.JobPosting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository 
        extends MongoRepository<JobPosting, String> {
}
