package com.hiredesk.service;

import com.hiredesk.dto.request.SubmitFeedbackRequest;
import com.hiredesk.dto.response.FeedbackResponse;
import com.hiredesk.model.Candidate;
import com.hiredesk.model.Feedback;
import com.hiredesk.model.InterviewRound;
import com.hiredesk.repository.CandidateRepository;
import com.hiredesk.repository.FeedbackRepository;
import com.hiredesk.repository.InterviewRoundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CandidateRepository candidateRepository;
    private final InterviewRoundRepository roundRepository;
    private final EmailService emailService;

    // Recruiter requests feedback after interview
    @Transactional
    public FeedbackResponse requestFeedback(
            Long candidateId, Long roundId) {

        Candidate candidate = candidateRepository
                .findById(candidateId)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        InterviewRound round = roundRepository
                .findById(roundId)
                .orElseThrow(() ->
                        new RuntimeException("Round not found"));

        // Check if feedback already exists
        if (feedbackRepository.existsByCandidateIdAndRoundId(
                candidateId, roundId)) {
            throw new RuntimeException(
                    "Feedback already requested for this round");
        }

        // Generate unique token for feedback form link
        String token = UUID.randomUUID().toString();

        Feedback feedback = Feedback.builder()
                .candidate(candidate)
                .round(round)
                .interviewer(round.getInterviewer())
                .feedbackToken(token)
                .isSubmitted(false)
                .build();

        Feedback saved = feedbackRepository.save(feedback);

        // Send email to interviewer (mocked for now)
        if (round.getInterviewer() != null) {
            String feedbackLink =
                    "http://localhost:3000/feedback/" + token;

            emailService.sendFeedbackRequest(
                    round.getInterviewer().getEmail(),
                    round.getInterviewer().getName(),
                    candidate.getName(),
                    feedbackLink
            );
        }

        return FeedbackResponse.fromEntity(saved);
    }

    // Interviewer opens feedback form via token (no login)
    public FeedbackResponse getFeedbackByToken(String token) {
        Feedback feedback = feedbackRepository
                .findByFeedbackToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid feedback link"));

        return FeedbackResponse.fromEntity(feedback);
    }

    // Interviewer submits feedback via token (no login)
    @Transactional
    public FeedbackResponse submitFeedback(
            String token, SubmitFeedbackRequest request) {

        Feedback feedback = feedbackRepository
                .findByFeedbackToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid feedback link"));

        if (feedback.getIsSubmitted()) {
            throw new RuntimeException(
                    "Feedback already submitted");
        }

        feedback.setTechnicalRating(request.getTechnicalRating());
        feedback.setCommunicationRating(
                request.getCommunicationRating());
        feedback.setProblemSolvingRating(
                request.getProblemSolvingRating());
        feedback.setAttitudeRating(request.getAttitudeRating());
        feedback.setWrittenNotes(request.getWrittenNotes());
        feedback.setStrengths(request.getStrengths());
        feedback.setWeaknesses(request.getWeaknesses());
        feedback.setRecommendation(request.getRecommendation());
        feedback.setIsSubmitted(true);
        feedback.setSubmittedAt(LocalDateTime.now());

        return FeedbackResponse.fromEntity(
                feedbackRepository.save(feedback));
    }

    // Get all feedback for a candidate
    public List<FeedbackResponse> getFeedbackByCandidate(
            Long candidateId) {
        return feedbackRepository
                .findByCandidateId(candidateId)
                .stream()
                .map(FeedbackResponse::fromEntity)
                .toList();
    }

    // Get all pending feedback (not submitted yet)
    public List<FeedbackResponse> getPendingFeedback() {
        return feedbackRepository
                .findByIsSubmittedFalse()
                .stream()
                .map(FeedbackResponse::fromEntity)
                .toList();
    }

    // Manually resend reminder to interviewer
    @Transactional
    public String sendReminder(Long feedbackId) {
        Feedback feedback = feedbackRepository
                .findById(feedbackId)
                .orElseThrow(() ->
                        new RuntimeException("Feedback not found"));

        if (feedback.getIsSubmitted()) {
            return "Feedback already submitted";
        }

        String feedbackLink = "http://localhost:3000/feedback/"
                + feedback.getFeedbackToken();

        emailService.sendFeedbackReminder(
                feedback.getInterviewer().getEmail(),
                feedback.getInterviewer().getName(),
                feedback.getCandidate().getName(),
                feedbackLink
        );

        return "Reminder sent to "
                + feedback.getInterviewer().getName();
    }
}