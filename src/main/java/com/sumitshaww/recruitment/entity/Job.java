package com.sumitshaww.recruitment.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "posted_on")
    private LocalDateTime postedOn;

    @Column(name = "total_applications")
    private Integer totalApplications = 0;

    @Column(name = "company_name")
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @PrePersist
    protected void onCreate() {
        postedOn = LocalDateTime.now();
    }
}