package com.hiredesk.service;

import com.hiredesk.dto.response.AnalyticsResponse;
import com.hiredesk.model.Candidate;
import com.hiredesk.model.JobOpening;
import com.hiredesk.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final JobRepository jobRepository;
    private final CandidateRepository candidateRepository;
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final InterviewRoundRepository roundRepository;

    // Overview — top dashboard cards
    public AnalyticsResponse getOverview() {

        long openJobs = jobRepository
                .findByStatus(JobOpening.JobStatus.OPEN).size();

        List<Candidate> allCandidates =
                candidateRepository.findAll();

        long totalCandidates = allCandidates.size();

        long selected = allCandidates.stream()
                .filter(c -> c.getStatus() ==
                        Candidate.CandidateStatus.SELECTED)
                .count();

        long rejected = allCandidates.stream()
                .filter(c -> c.getStatus() ==
                        Candidate.CandidateStatus.REJECTED)
                .count();

        long pendingFeedbacks = feedbackRepository
                .findByIsSubmittedFalse().size();

        return AnalyticsResponse.builder()
                .totalOpenJobs(openJobs)
                .totalCandidates(totalCandidates)
                .totalSelected(selected)
                .totalRejected(rejected)
                .pendingFeedbacks(pendingFeedbacks)
                .sourceBreakdown(getSourceBreakdown())
                .interviewerStats(getInterviewerStats())
                .build();
    }

    // Funnel — drop off at each stage for a job
    public List<AnalyticsResponse.FunnelData> getFunnel(
            Long jobId) {

        JobOpening job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        List<Candidate> candidates =
                candidateRepository.findByJobOpeningId(jobId);

        long totalApplied = candidates.size();

        List<AnalyticsResponse.FunnelData> funnel =
                new ArrayList<>();

        // Stage 0 — Applied
        funnel.add(AnalyticsResponse.FunnelData.builder()
                .stage(0)
                .stageName("Applied")
                .count(totalApplied)
                .percentage(100.0)
                .build());

        // Each round stage
        for (int i = 1; i <= job.getTotalRounds(); i++) {
            final int stage = i;
            long count = candidates.stream()
                    .filter(c -> c.getCurrentStage() >= stage
                            && c.getStatus() !=
                            Candidate.CandidateStatus.REJECTED)
                    .count();

            double percentage = totalApplied > 0
                    ? (count * 100.0) / totalApplied
                    : 0;

            funnel.add(AnalyticsResponse.FunnelData.builder()
                    .stage(i)
                    .stageName("Round " + i)
                    .count(count)
                    .percentage(
                            Math.round(percentage * 10.0) / 10.0)
                    .build());
        }

        // Selected
        long selectedCount = candidates.stream()
                .filter(c -> c.getStatus() ==
                        Candidate.CandidateStatus.SELECTED)
                .count();

        double selectedPct = totalApplied > 0
                ? (selectedCount * 100.0) / totalApplied : 0;

        funnel.add(AnalyticsResponse.FunnelData.builder()
                .stage(job.getTotalRounds() + 1)
                .stageName("Selected")
                .count(selectedCount)
                .percentage(
                        Math.round(selectedPct * 10.0) / 10.0)
                .build());

        return funnel;
    }

    // Source breakdown — LinkedIn vs Referral vs Campus
    public Map<String, Long> getSourceBreakdown() {
        List<Candidate> all = candidateRepository.findAll();

        return all.stream()
                .filter(c -> c.getSource() != null)
                .collect(Collectors.groupingBy(
                        c -> c.getSource().name(),
                        Collectors.counting()
                ));
    }

    // Interviewer stats — feedback submission rates
    public List<AnalyticsResponse.InterviewerStat>
    getInterviewerStats() {

        return userRepository.findAll().stream()
                .filter(u -> u.getRole() ==
                        com.hiredesk.model.User.Role.INTERVIEWER)
                .map(interviewer -> {

                    long total = feedbackRepository
                            .findAll()
                            .stream()
                            .filter(f -> f.getInterviewer() != null
                                    && f.getInterviewer().getId()
                                    .equals(interviewer.getId()))
                            .count();

                    long submitted = feedbackRepository
                            .findAll()
                            .stream()
                            .filter(f -> f.getInterviewer() != null
                                    && f.getInterviewer().getId()
                                    .equals(interviewer.getId())
                                    && f.getIsSubmitted())
                            .count();

                    double rate = total > 0
                            ? (submitted * 100.0) / total : 0;

                    return AnalyticsResponse.InterviewerStat
                            .builder()
                            .interviewerId(interviewer.getId())
                            .interviewerName(interviewer.getName())
                            .totalAssigned(total)
                            .submitted(submitted)
                            .submissionRate(
                                    Math.round(rate * 10.0) / 10.0)
                            .build();
                })
                .toList();
    }

    // Monthly hiring trend
    public Map<String, Long> getMonthlyTrend() {
        List<Candidate> selected = candidateRepository
                .findAll()
                .stream()
                .filter(c -> c.getStatus() ==
                        Candidate.CandidateStatus.SELECTED
                        && c.getCreatedAt() != null)
                .toList();

        return selected.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getCreatedAt()
                                .getMonth()
                                .name()
                                .substring(0, 3),
                        Collectors.counting()
                ));
    }
}