package com.sumitshaww.recruitment.repository;

import com.sumitshaww.recruitment.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}