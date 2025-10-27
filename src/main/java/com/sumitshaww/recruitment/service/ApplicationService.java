package com.sumitshaww.recruitment.service;

import com.sumitshaww.recruitment.entity.Application;
import com.sumitshaww.recruitment.entity.Job;
import com.sumitshaww.recruitment.entity.User;
import com.sumitshaww.recruitment.repository.ApplicationRepository;
import com.sumitshaww.recruitment.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    public Application applyForJob(Long jobId, User applicant) {
        // Check if already applied
        if (applicationRepository.existsByJobIdAndApplicantId(jobId, applicant.getId())) {
            throw new RuntimeException("Already applied for this job");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Application application = new Application();
        application.setJob(job);
        application.setApplicant(applicant);

        // Update job application count
        job.setTotalApplications(job.getTotalApplications() + 1);
        jobRepository.save(job);

        return applicationRepository.save(application);
    }

    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }
}