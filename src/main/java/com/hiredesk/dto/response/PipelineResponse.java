package com.hiredesk.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class PipelineResponse {

    private Long jobId;
    private String jobTitle;
    private Integer totalRounds;
    private Map<Integer, List<CandidateResponse>> stages;
    private Long totalCandidates;
    private Long selected;
    private Long rejected;
}