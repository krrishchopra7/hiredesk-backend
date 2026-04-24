package com.hiredesk.controller;

import com.hiredesk.dto.request.CreateCandidateRequest;
import com.hiredesk.dto.response.CandidateResponse;
import com.hiredesk.dto.response.PipelineResponse;
import com.hiredesk.model.Candidate;
import com.hiredesk.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateResponse> addCandidate(
            @Valid @RequestBody CreateCandidateRequest request,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                candidateService.addCandidate(request, email));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<CandidateResponse>> getByJob(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(
                candidateService.getCandidatesByJob(jobId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponse> getById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                candidateService.getCandidateById(id));
    }

    @PatchMapping("/{id}/stage")
    public ResponseEntity<CandidateResponse> updateStage(
            @PathVariable Long id,
            @RequestParam Integer stage) {
        return ResponseEntity.ok(
                candidateService.updateStage(id, stage));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CandidateResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Candidate.CandidateStatus status) {
        return ResponseEntity.ok(
                candidateService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidate(
            @PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok("Candidate deleted");
    }

    @GetMapping("/pipeline/{jobId}")
    public ResponseEntity<PipelineResponse> getPipeline(
            @PathVariable Long jobId) {
        return ResponseEntity.ok(
                candidateService.getPipeline(jobId));
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<Map<String, Object>> bulkUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long jobId,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(
                candidateService.bulkUpload(file, jobId, email));
    }
}