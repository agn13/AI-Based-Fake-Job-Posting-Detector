package com.fakejobdetector.controller;

import com.fakejobdetector.model.JobPosting;
import com.fakejobdetector.service.JobAnalysisService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:5173")
public class JobAnalysisController {

    private final JobAnalysisService service;

    public JobAnalysisController(JobAnalysisService service) {
        this.service = service;
    }

    // ==============================
    // Analyze Job
    // ==============================
    @PostMapping("/analyze")
    public ResponseEntity<JobPosting> analyze(
            @RequestBody JobPosting job) {

        return ResponseEntity.ok(service.analyze(job));
    }

    // ==============================
    // Get Jobs (Pagination + Filter + Sort)
    // ==============================
    @GetMapping
    public ResponseEntity<Page<JobPosting>> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "fraudScore") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String riskLevel
    ) {
        return ResponseEntity.ok(
                service.getAllJobs(page, size, sortBy, direction, riskLevel)
        );
    }

    // ==============================
    // Dashboard Stats
    // ==============================
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(service.getStats());
    }
    // ==============================
    // Delete Job
    // ==============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable String id) {
        service.deleteJob(id);
        return ResponseEntity.ok("Deleted successfully");
    }
  
    // ==============================
    // Export as PDF
    // ==============================
    @GetMapping("/export")
    public ResponseEntity<String> exportCsv() {

    var jobs = service.getAllJobs(0, Integer.MAX_VALUE,
            "fraudScore", "desc", null).getContent();

    StringBuilder csv = new StringBuilder();
    csv.append("Job Title,Email,Salary,Score,Risk\n");

    for (var job : jobs) {
        csv.append(job.getJobTitle()).append(",")
                .append(job.getCompanyEmail()).append(",")
                .append(job.getSalary()).append(",")
                .append(job.getFraudScore()).append(",")
                .append(job.getRiskLevel())
                .append("\n");
    }

    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=jobs.csv")
            .body(csv.toString());
    }

}
