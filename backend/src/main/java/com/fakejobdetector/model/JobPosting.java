package com.fakejobdetector.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "job_analysis")
public class JobPosting {

    @Id
    private String id;

    private String jobTitle;
    private String companyEmail;
    private String salary;
    private String description;

    private Double fraudScore;
    private String riskLevel;
    private List<String> reasons;

    private LocalDateTime analyzedAt;

    public JobPosting() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompanyEmail() { return companyEmail; }
    public void setCompanyEmail(String companyEmail) { this.companyEmail = companyEmail; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getFraudScore() { return fraudScore; }
    public void setFraudScore(Double fraudScore) { this.fraudScore = fraudScore; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }

    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }
}
