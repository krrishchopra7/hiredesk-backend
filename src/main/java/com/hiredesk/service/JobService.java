package com.hiredesk.service;

import com.hiredesk.dto.request.CreateJobRequest;
import com.hiredesk.dto.response.JobResponse;
import com.hiredesk.model.InterviewRound;
import com.hiredesk.model.JobOpening;
import com.hiredesk.model.User;
import com.hiredesk.repository.CandidateRepository;
import com.hiredesk.repository.InterviewRoundRepository;
import com.hiredesk.repository.JobRepository;
import com.hiredesk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final InterviewRoundRepository roundRepository;
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;

    @Transactional
    public JobResponse createJob(CreateJobRequest request, String email) {

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobOpening job = JobOpening.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .department(request.getDepartment())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .deadline(request.getDeadline())
                .totalRounds(request.getRounds().size())
                .createdBy(recruiter)
                .build();

        JobOpening savedJob = jobRepository.save(job);

        // Save interview rounds
        List<InterviewRound> rounds = request.getRounds().stream()
                .map(r -> {
                    User interviewer = null;
                    if (r.getInterviewerId() != null) {
                        interviewer = userRepository
                                .findById(r.getInterviewerId())
                                .orElse(null);
                    }
                    return InterviewRound.builder()
                            .jobOpening(savedJob)
                            .roundNumber(r.getRoundNumber())
                            .roundName(r.getRoundName())
                            .roundType(r.getRoundType())
                            .interviewer(interviewer)
                            .build();
                })
                .toList();

        roundRepository.saveAll(rounds);

        return buildJobResponse(savedJob, rounds);
    }

    public List<JobResponse> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(job -> {
                    List<InterviewRound> rounds =
                            roundRepository
                                    .findByJobOpeningIdOrderByRoundNumber(job.getId());
                    return buildJobResponse(job, rounds);
                })
                .toList();
    }

    public JobResponse getJobById(Long id) {
        JobOpening job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found with id: " + id));

        List<InterviewRound> rounds =
                roundRepository
                        .findByJobOpeningIdOrderByRoundNumber(id);

        return buildJobResponse(job, rounds);
    }

    @Transactional
    public JobResponse updateJobStatus(Long id,
                                       JobOpening.JobStatus status) {
        JobOpening job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        job.setStatus(status);
        jobRepository.save(job);

        List<InterviewRound> rounds =
                roundRepository
                        .findByJobOpeningIdOrderByRoundNumber(id);

        return buildJobResponse(job, rounds);
    }

    @Transactional
    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found");
        }
        roundRepository.deleteByJobOpeningId(id);
        jobRepository.deleteById(id);
    }

    private JobResponse buildJobResponse(JobOpening job,
                                         List<InterviewRound> rounds) {
        Long candidateCount = candidateRepository
                .countByJobOpeningId(job.getId());
        return JobResponse.fromEntity(job, rounds, candidateCount);
    }
}