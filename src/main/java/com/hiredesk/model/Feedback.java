package com.hiredesk.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private InterviewRound round;

    @ManyToOne
    @JoinColumn(name = "interviewer_id")
    private User interviewer;

    private Integer technicalRating;
    private Integer communicationRating;
    private Integer problemSolvingRating;
    private Integer attitudeRating;

    @Column(columnDefinition = "TEXT")
    private String writtenNotes;

    private String strengths;
    private String weaknesses;

    @Enumerated(EnumType.STRING)
    private Recommendation recommendation;

    private String feedbackToken;
    private Boolean isSubmitted = false;
    private LocalDateTime submittedAt;

    public enum Recommendation {
        STRONG_YES, YES, MAYBE, NO, STRONG_NO
    }
}