package com.hiredesk.controller;

import com.hiredesk.dto.request.CreateJobRequest;
import com.hiredesk.dto.response.JobResponse;
import com.hiredesk.model.JobOpening;
import com.hiredesk.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(jobService.createJob(request, email));
    }

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam JobOpening.JobStatus status) {
        return ResponseEntity.ok(
                jobService.updateJobStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully");
    }
}
