package com.hiredesk.controller;

import com.hiredesk.dto.response.AnalyticsResponse;
import com.hiredesk.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // Main dashboard overview
    @GetMapping("/overview")
    public ResponseEntity<AnalyticsResponse> getOverview() {
        return ResponseEntity.ok(
                analyticsService.getOverview());
    }

    // Funnel for specific job
    @GetMapping("/funnel/{jobId}")
    public ResponseEntity<List<AnalyticsResponse.FunnelData>>
    getFunnel(@PathVariable Long jobId) {
        return ResponseEntity.ok(
                analyticsService.getFunnel(jobId));
    }

    // Source breakdown
    @GetMapping("/sources")
    public ResponseEntity<Map<String, Long>>
    getSourceBreakdown() {
        return ResponseEntity.ok(
                analyticsService.getSourceBreakdown());
    }

    // Interviewer performance stats
    @GetMapping("/interviewers")
    public ResponseEntity<List<AnalyticsResponse.InterviewerStat>> getInterviewerStats() {
        return ResponseEntity.ok(analyticsService.getInterviewerStats());
    }

    // Monthly hiring trend
    @GetMapping("/trend")
    public ResponseEntity<Map<String, Long>>
    getMonthlyTrend() {
        return ResponseEntity.ok(
                analyticsService.getMonthlyTrend());
    }
}