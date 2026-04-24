package com.hiredesk.repository;

import com.hiredesk.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository
        extends JpaRepository<Feedback, Long> {

    List<Feedback> findByCandidateId(Long candidateId);

    Optional<Feedback> findByFeedbackToken(String token);

    List<Feedback> findByIsSubmittedFalse();

    List<Feedback> findByInterviewerIdAndIsSubmittedFalse(
            Long interviewerId);

    boolean existsByCandidateIdAndRoundId(
            Long candidateId, Long roundId);
}