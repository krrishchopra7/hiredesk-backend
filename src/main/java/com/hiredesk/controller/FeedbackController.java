package com.hiredesk.controller;

import com.hiredesk.dto.request.SubmitFeedbackRequest;
import com.hiredesk.dto.response.FeedbackResponse;
import com.hiredesk.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    // Recruiter requests feedback
    // after marking interview complete
    @PostMapping("/request")
    public ResponseEntity<FeedbackResponse> requestFeedback(
            @RequestParam Long candidateId,
            @RequestParam Long roundId) {
        return ResponseEntity.ok(
                feedbackService.requestFeedback(
                        candidateId, roundId));
    }

    // Interviewer opens form via link — NO LOGIN NEEDED
    @GetMapping("/form/{token}")
    public ResponseEntity<FeedbackResponse> getFeedbackForm(
            @PathVariable String token) {
        return ResponseEntity.ok(
                feedbackService.getFeedbackByToken(token));
    }

    // Interviewer submits feedback — NO LOGIN NEEDED
    @PostMapping("/submit/{token}")
    public ResponseEntity<FeedbackResponse> submitFeedback(
            @PathVariable String token,
            @Valid @RequestBody SubmitFeedbackRequest request) {
        return ResponseEntity.ok(
                feedbackService.submitFeedback(token, request));
    }

    // Get all feedback for a candidate
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<FeedbackResponse>> getByCandidate(
            @PathVariable Long candidateId) {
        return ResponseEntity.ok(
                feedbackService.getFeedbackByCandidate(candidateId));
    }

    // Get all pending feedback
    @GetMapping("/pending")
    public ResponseEntity<List<FeedbackResponse>> getPending() {
        return ResponseEntity.ok(
                feedbackService.getPendingFeedback());
    }

    // Send reminder to interviewer
    @PostMapping("/remind/{feedbackId}")
    public ResponseEntity<String> sendReminder(
            @PathVariable Long feedbackId) {
        return ResponseEntity.ok(
                feedbackService.sendReminder(feedbackId));
    }
}