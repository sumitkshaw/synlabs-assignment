package com.sumitshaww.recruitment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked") // Suppresses the unchecked assignment warning
public class ResumeParserService {

    @Value("${resume.parser.url}")
    private String resumeParserUrl; // This will be assigned by Spring

    @Value("${resume.parser.apikey}")
    private String apiKey; // This will be assigned by Spring

    private final WebClient webClient = WebClient.builder().build();

    public Map<String, Object> parseResume(byte[] fileBytes) {
        try {
            // The warning is suppressed with @SuppressWarnings
            return webClient.post()
                    .uri(resumeParserUrl)
                    .header("apikey", apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .bodyValue(fileBytes)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse resume: " + e.getMessage());
        }
    }
}