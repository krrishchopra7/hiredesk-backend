package com.hiredesk.dto.response;

import com.hiredesk.model.InterviewRound;
import com.hiredesk.model.JobOpening;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class JobResponse {

    private Long id;
    private String title;
    private String description;
    private String department;
    private String location;
    private String jobType;
    private String status;
    private LocalDate deadline;
    private Integer totalRounds;
    private String createdByName;
    private LocalDateTime createdAt;
    private List<RoundResponse> rounds;
    private Long candidateCount;

    @Data
    @Builder
    public static class RoundResponse {
        private Long id;
        private Integer roundNumber;
        private String roundName;
        private String roundType;
        private String interviewerName;
    }

    public static JobResponse fromEntity(
            JobOpening job,
            List<InterviewRound> rounds,
            Long candidateCount) {

        List<RoundResponse> roundResponses = rounds.stream()
                .map(r -> RoundResponse.builder()
                        .id(r.getId())
                        .roundNumber(r.getRoundNumber())
                        .roundName(r.getRoundName())
                        .roundType(r.getRoundType().name())
                        .interviewerName(r.getInterviewer() != null ?
                                r.getInterviewer().getName() : null)
                        .build())
                .toList();

        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .department(job.getDepartment())
                .location(job.getLocation())
                .jobType(job.getJobType().name())
                .status(job.getStatus().name())
                .deadline(job.getDeadline())
                .totalRounds(job.getTotalRounds())
                .createdByName(job.getCreatedBy() != null ?
                        job.getCreatedBy().getName() : null)
                .createdAt(job.getCreatedAt())
                .rounds(roundResponses)
                .candidateCount(candidateCount)
                .build();
    }
}