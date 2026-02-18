package com.fakejobdetector.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
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
}
