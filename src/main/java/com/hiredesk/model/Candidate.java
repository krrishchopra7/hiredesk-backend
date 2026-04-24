package com.hiredesk.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String phone;
    private String resumeUrl;
    private String currentCompany;
    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    private Source source;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobOpening jobOpening;

    private Integer currentStage = 0;

    @Enumerated(EnumType.STRING)
    private CandidateStatus status;

    @ManyToOne
    @JoinColumn(name = "added_by")
    private User addedBy;

    private String schedulingToken;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = CandidateStatus.IN_PIPELINE;
    }

    public enum Source {
        LINKEDIN, REFERRAL, CAMPUS, PORTAL, WALK_IN
    }

    public enum CandidateStatus {
        IN_PIPELINE, SELECTED, REJECTED, WITHDRAWN, ON_HOLD
    }
}