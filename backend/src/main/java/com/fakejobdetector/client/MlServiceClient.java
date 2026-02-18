package com.fakejobdetector.client;

import com.fakejobdetector.model.JobAnalysisResponse;
import com.fakejobdetector.model.JobPosting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MlServiceClient {

    private final WebClient webClient;

    public MlServiceClient(WebClient.Builder builder,
                           @Value("${ml.service.url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public JobAnalysisResponse analyze(JobPosting job) {
        return webClient.post()
                .uri("/predict")
                .bodyValue(job)
                .retrieve()
                .bodyToMono(JobAnalysisResponse.class)
                .block();
    }
}
