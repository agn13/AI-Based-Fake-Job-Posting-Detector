package com.fakejobdetector.model;

import java.util.List;

public class JobAnalysisResponse {

    private Double fraudScore;
    private String riskLevel;
    private List<String> reasons;

    public JobAnalysisResponse() {}

    public Double getFraudScore() { return fraudScore; }
    public void setFraudScore(Double fraudScore) { this.fraudScore = fraudScore; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public List<String> getReasons() { return reasons; }
    public void setReasons(List<String> reasons) { this.reasons = reasons; }
}
