package com.hiredesk.repository;

import com.hiredesk.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateRepository
        extends JpaRepository<Candidate, Long> {

    List<Candidate> findByJobOpeningId(Long jobId);

    Long countByJobOpeningId(Long jobId);

    Optional<Candidate> findBySchedulingToken(String token);

    List<Candidate> findByJobOpeningIdAndStatus(
            Long jobId,
            Candidate.CandidateStatus status);
}