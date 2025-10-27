package com.sumitshaww.recruitment.controller;

import com.sumitshaww.recruitment.dto.JobRequest;
import com.sumitshaww.recruitment.entity.Job;
import com.sumitshaww.recruitment.entity.User;
import com.sumitshaww.recruitment.repository.JobRepository;
import com.sumitshaww.recruitment.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobRepository jobRepository;
    private final ApplicationService applicationService;

    @PostMapping("/admin/job")
    public ResponseEntity<Job> createJob(@RequestBody JobRequest request, @AuthenticationPrincipal User user) {
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompanyName(request.getCompanyName());
        job.setPostedBy(user);

        Job savedJob = jobRepository.save(job);
        return ResponseEntity.ok(savedJob);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/admin/job/{jobId}")
    public ResponseEntity<Map<String, Object>> getJobWithApplicants(@PathVariable Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        var applications = applicationService.getApplicationsByJob(jobId);

        Map<String, Object> response = Map.of(
                "job", job,
                "applicants", applications
        );

        return ResponseEntity.ok(response);
    }
}