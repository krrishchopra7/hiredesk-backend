package com.hiredesk.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interview_rounds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobOpening jobOpening;

    private Integer roundNumber;
    private String roundName;

    @Enumerated(EnumType.STRING)
    private RoundType roundType;

    @ManyToOne
    @JoinColumn(name = "interviewer_id")
    private User interviewer;

    public enum RoundType {
        TECHNICAL, HR, ASSIGNMENT, GROUP_DISCUSSION
    }
}