package com.hiredesk.dto.request;

import com.hiredesk.model.JobOpening;
import com.hiredesk.model.InterviewRound;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateJobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String department;
    private String location;

    @NotNull(message = "Job type is required")
    private JobOpening.JobType jobType;

    private LocalDate deadline;

    @NotNull(message = "Rounds are required")
    private List<RoundRequest> rounds;

    @Data
    public static class RoundRequest {
        private Integer roundNumber;
        private String roundName;
        private InterviewRound.RoundType roundType;
        private Long interviewerId;
    }
}