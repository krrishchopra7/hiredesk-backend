package com.hiredesk.dto.response;

import com.hiredesk.model.Feedback;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponse {

    private Long id;
    private Long candidateId;
    private String candidateName;
    private Long roundId;
    private String roundName;
    private Long interviewerId;
    private String interviewerName;
    private Integer technicalRating;
    private Integer communicationRating;
    private Integer problemSolvingRating;
    private Integer attitudeRating;
    private Double averageRating;
    private String writtenNotes;
    private String strengths;
    private String weaknesses;
    private String recommendation;
    private Boolean isSubmitted;
    private LocalDateTime submittedAt;
    private String feedbackToken;

    public static FeedbackResponse fromEntity(Feedback f) {

        Double avg = null;
        if (f.getTechnicalRating() != null) {
            avg = (f.getTechnicalRating() +
                    f.getCommunicationRating() +
                    f.getProblemSolvingRating() +
                    f.getAttitudeRating()) / 4.0;
        }

        return FeedbackResponse.builder()
                .id(f.getId())
                .candidateId(f.getCandidate() != null ?
                        f.getCandidate().getId() : null)
                .candidateName(f.getCandidate() != null ?
                        f.getCandidate().getName() : null)
                .roundId(f.getRound() != null ?
                        f.getRound().getId() : null)
                .roundName(f.getRound() != null ?
                        f.getRound().getRoundName() : null)
                .interviewerId(f.getInterviewer() != null ?
                        f.getInterviewer().getId() : null)
                .interviewerName(f.getInterviewer() != null ?
                        f.getInterviewer().getName() : null)
                .technicalRating(f.getTechnicalRating())
                .communicationRating(f.getCommunicationRating())
                .problemSolvingRating(f.getProblemSolvingRating())
                .attitudeRating(f.getAttitudeRating())
                .averageRating(avg)
                .writtenNotes(f.getWrittenNotes())
                .strengths(f.getStrengths())
                .weaknesses(f.getWeaknesses())
                .recommendation(f.getRecommendation() != null ?
                        f.getRecommendation().name() : null)
                .isSubmitted(f.getIsSubmitted())
                .submittedAt(f.getSubmittedAt())
                .feedbackToken(f.getFeedbackToken())
                .build();
    }
}