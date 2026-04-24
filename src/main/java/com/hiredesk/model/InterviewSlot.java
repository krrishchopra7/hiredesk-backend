package com.hiredesk.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSlot {

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

    private String proposedTimes;
    private LocalDateTime bookedTime;
    private String meetingLink;
    private String schedulingToken;
    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.slotStatus = SlotStatus.PENDING_BOOKING;
    }

    public enum SlotStatus {
        PENDING_BOOKING, BOOKED, COMPLETED, CANCELLED
    }
}