package com.fakejobdetector.service;

import com.fakejobdetector.client.MlServiceClient;
import com.fakejobdetector.model.JobAnalysisResponse;
import com.fakejobdetector.model.JobPosting;
import com.fakejobdetector.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JobAnalysisService {

    private final MlServiceClient mlClient;
    private final JobRepository repository;

    public JobAnalysisService(MlServiceClient mlClient,
                              JobRepository repository) {
        this.mlClient = mlClient;
        this.repository = repository;
    }

    public JobPosting analyze(JobPosting job) {

        JobAnalysisResponse response = mlClient.analyze(job);

        job.setFraudScore(response.getFraudScore());
        job.setRiskLevel(response.getRiskLevel());
        job.setReasons(response.getReasons());
        job.setAnalyzedAt(LocalDateTime.now());

        return repository.save(job);
    }
}
