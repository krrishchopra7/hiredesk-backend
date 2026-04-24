package com.hiredesk.repository;

import com.hiredesk.model.InterviewSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewSlotRepository
        extends JpaRepository<InterviewSlot, Long> {

    List<InterviewSlot> findByCandidateId(Long candidateId);

    Optional<InterviewSlot> findBySchedulingToken(String token);

    List<InterviewSlot> findByInterviewerIdAndSlotStatus(
            Long interviewerId,
            InterviewSlot.SlotStatus status);
}