package com.fakejobdetector.service;

import com.fakejobdetector.client.MlServiceClient;
import com.fakejobdetector.model.JobAnalysisResponse;
import com.fakejobdetector.model.JobPosting;
import com.fakejobdetector.repository.JobRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class JobAnalysisService {

    private final MlServiceClient mlClient;
    private final JobRepository repository;

    public JobAnalysisService(MlServiceClient mlClient,
                              JobRepository repository) {
        this.mlClient = mlClient;
        this.repository = repository;
    }

    // ==============================
    // Analyze Job (Save to MongoDB)
    // ==============================
    public JobPosting analyze(JobPosting job) {

        JobAnalysisResponse response = mlClient.analyze(job);

        job.setFraudScore(response.getFraudScore());
        job.setRiskLevel(response.getRiskLevel());
        job.setReasons(response.getReasons());
        job.setAnalyzedAt(LocalDateTime.now());

        return repository.save(job);
    }

    // ==============================
    // Get Jobs with Pagination + Filter + Sort
    // ==============================
    public Page<JobPosting> getAllJobs(
            int page,
            int size,
            String sortBy,
            String direction,
            String riskLevel
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (riskLevel != null && !riskLevel.isEmpty()) {
            return repository.findByRiskLevel(riskLevel, pageable);
        }

        return repository.findAll(pageable);
    }

    // ==============================
    // Analytics / Stats
    // ==============================
    public Map<String, Long> getStats() {

        long total = repository.count();

        long high = repository.findAll()
                .stream()
                .filter(j -> "HIGH".equals(j.getRiskLevel()))
                .count();

        long medium = repository.findAll()
                .stream()
                .filter(j -> "MEDIUM".equals(j.getRiskLevel()))
                .count();

        long low = repository.findAll()
                .stream()
                .filter(j -> "LOW".equals(j.getRiskLevel()))
                .count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("highRisk", high);
        stats.put("mediumRisk", medium);
        stats.put("lowRisk", low);

        return stats;
    }
    //Deletes job
    public void deleteJob(String id) {
        repository.deleteById(id);
    }

}
