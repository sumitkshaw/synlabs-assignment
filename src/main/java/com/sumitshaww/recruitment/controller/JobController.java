package com.sumitshaww.recruitment.controller;

import com.sumitshaww.recruitment.dto.JobRequest;
import com.sumitshaww.recruitment.entity.Job;
import com.sumitshaww.recruitment.entity.User;
import com.sumitshaww.recruitment.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JobController {

    private final JobRepository jobRepository;

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
    public ResponseEntity<Job> getJobById(@PathVariable Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        return ResponseEntity.ok(job);
    }
}