package com.hiredesk.repository;

import com.hiredesk.model.InterviewRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRoundRepository
        extends JpaRepository<InterviewRound, Long> {

    List<InterviewRound> findByJobOpeningIdOrderByRoundNumber(Long jobId);

    void deleteByJobOpeningId(Long jobId);
}