package com.fakejobdetector.controller;

import com.fakejobdetector.model.JobPosting;
import com.fakejobdetector.service.JobAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:5173")
public class JobAnalysisController {

    private final JobAnalysisService service;

    public JobAnalysisController(JobAnalysisService service) {
        this.service = service;
    }

    @PostMapping("/analyze")
    public ResponseEntity<JobPosting> analyze(
            @RequestBody JobPosting job) {

        return ResponseEntity.ok(service.analyze(job));
    }
}
