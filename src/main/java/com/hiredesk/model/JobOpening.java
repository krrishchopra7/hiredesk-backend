package com.hiredesk.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_openings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobOpening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String department;
    private String location;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private Integer totalRounds;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL)
    private List<InterviewRound> rounds;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = JobStatus.OPEN;
    }

    public enum JobType {
        FULL_TIME, INTERNSHIP, CONTRACT
    }

    public enum JobStatus {
        OPEN, CLOSED, ON_HOLD
    }
}