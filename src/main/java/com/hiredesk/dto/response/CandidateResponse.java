package com.hiredesk.dto.response;

import com.hiredesk.model.Candidate;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CandidateResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String currentCompany;
    private Integer experienceYears;
    private String source;
    private String status;
    private Integer currentStage;
    private Long jobId;
    private String jobTitle;
    private String resumeUrl;
    private String schedulingToken;
    private LocalDateTime createdAt;

    public static CandidateResponse fromEntity(Candidate c) {
        return CandidateResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .email(c.getEmail())
                .phone(c.getPhone())
                .currentCompany(c.getCurrentCompany())
                .experienceYears(c.getExperienceYears())
                .source(c.getSource() != null ?
                        c.getSource().name() : null)
                .status(c.getStatus() != null ?
                        c.getStatus().name() : null)
                .currentStage(c.getCurrentStage())
                .jobId(c.getJobOpening() != null ?
                        c.getJobOpening().getId() : null)
                .jobTitle(c.getJobOpening() != null ?
                        c.getJobOpening().getTitle() : null)
                .resumeUrl(c.getResumeUrl())
                .schedulingToken(c.getSchedulingToken())
                .createdAt(c.getCreatedAt())
                .build();
    }
}