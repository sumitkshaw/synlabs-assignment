package com.sumitshaww.recruitment.repository;

import com.sumitshaww.recruitment.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobId(Long jobId);
    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);
}