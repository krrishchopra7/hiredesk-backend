package com.hiredesk.dto.request;

import com.hiredesk.model.Feedback;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitFeedbackRequest {

    @NotNull
    @Min(1) @Max(5)
    private Integer technicalRating;

    @NotNull
    @Min(1) @Max(5)
    private Integer communicationRating;

    @NotNull
    @Min(1) @Max(5)
    private Integer problemSolvingRating;

    @NotNull
    @Min(1) @Max(5)
    private Integer attitudeRating;

    private String writtenNotes;
    private String strengths;
    private String weaknesses;

    @NotNull
    private Feedback.Recommendation recommendation;
}