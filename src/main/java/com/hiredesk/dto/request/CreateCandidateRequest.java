package com.hiredesk.dto.request;

import com.hiredesk.model.Candidate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCandidateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;
    private String currentCompany;
    private Integer experienceYears;

    @NotNull(message = "Source is required")
    private Candidate.Source source;

    @NotNull(message = "Job ID is required")
    private Long jobId;
}