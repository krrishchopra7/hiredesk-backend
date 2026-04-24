package com.hiredesk.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AnalyticsResponse {

    // Overview cards
    private Long totalOpenJobs;
    private Long totalCandidates;
    private Long totalSelected;
    private Long totalRejected;
    private Long pendingFeedbacks;
    private Double averageTimeToHire;

    // Funnel data per job
    private List<FunnelData> funnel;

    // Source breakdown
    private Map<String, Long> sourceBreakdown;

    // Interviewer stats
    private List<InterviewerStat> interviewerStats;

    // Monthly hiring trend
    private Map<String, Long> monthlyTrend;

    @Data
    @Builder
    public static class FunnelData {
        private Integer stage;
        private String stageName;
        private Long count;
        private Double percentage;
    }

    @Data
    @Builder
    public static class InterviewerStat {
        private Long interviewerId;
        private String interviewerName;
        private Long totalAssigned;
        private Long submitted;
        private Double submissionRate;
    }
}