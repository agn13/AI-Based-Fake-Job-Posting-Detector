package com.fakejobdetector.model;

import lombok.Data;
import java.util.List;

@Data
public class JobAnalysisResponse {
    private Double fraudScore;
    private String riskLevel;
    private List<String> reasons;
}
