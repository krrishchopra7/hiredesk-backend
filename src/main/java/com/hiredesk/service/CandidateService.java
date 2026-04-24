package com.hiredesk.service;

import com.hiredesk.dto.request.CreateCandidateRequest;
import com.hiredesk.dto.response.CandidateResponse;
import com.hiredesk.dto.response.PipelineResponse;
import com.hiredesk.model.Candidate;
import com.hiredesk.model.JobOpening;
import com.hiredesk.model.User;
import com.hiredesk.repository.CandidateRepository;
import com.hiredesk.repository.JobRepository;
import com.hiredesk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // Add single candidate
    @Transactional
    public CandidateResponse addCandidate(
            CreateCandidateRequest request, String email) {

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        JobOpening job = jobRepository.findById(request.getJobId())
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        Candidate candidate = Candidate.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .currentCompany(request.getCurrentCompany())
                .experienceYears(request.getExperienceYears())
                .source(request.getSource())
                .jobOpening(job)
                .currentStage(0)
                .addedBy(recruiter)
                .schedulingToken(UUID.randomUUID().toString())
                .build();

        return CandidateResponse.fromEntity(
                candidateRepository.save(candidate));
    }

    // Get all candidates for a job
    public List<CandidateResponse> getCandidatesByJob(Long jobId) {
        return candidateRepository.findByJobOpeningId(jobId)
                .stream()
                .map(CandidateResponse::fromEntity)
                .toList();
    }

    // Get single candidate
    public CandidateResponse getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));
        return CandidateResponse.fromEntity(candidate);
    }

    // Move candidate to next stage (Kanban drag)
    @Transactional
    public CandidateResponse updateStage(Long id, Integer stage) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        candidate.setCurrentStage(stage);
        return CandidateResponse.fromEntity(
                candidateRepository.save(candidate));
    }

    // Update candidate status
    @Transactional
    public CandidateResponse updateStatus(
            Long id, Candidate.CandidateStatus status) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Candidate not found"));

        candidate.setStatus(status);
        return CandidateResponse.fromEntity(
                candidateRepository.save(candidate));
    }

    // Delete candidate
    @Transactional
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new RuntimeException("Candidate not found");
        }
        candidateRepository.deleteById(id);
    }

    // Get pipeline — Kanban view
    public PipelineResponse getPipeline(Long jobId) {
        JobOpening job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        List<Candidate> allCandidates =
                candidateRepository.findByJobOpeningId(jobId);

        // Group candidates by their current stage
        Map<Integer, List<CandidateResponse>> stages =
                allCandidates.stream()
                        .filter(c -> c.getStatus() ==
                                Candidate.CandidateStatus.IN_PIPELINE)
                        .collect(Collectors.groupingBy(
                                Candidate::getCurrentStage,
                                Collectors.mapping(
                                        CandidateResponse::fromEntity,
                                        Collectors.toList()
                                )
                        ));

        // Make sure all stages exist even if empty
        for (int i = 0; i <= job.getTotalRounds(); i++) {
            stages.putIfAbsent(i, new ArrayList<>());
        }

        long selected = allCandidates.stream()
                .filter(c -> c.getStatus() ==
                        Candidate.CandidateStatus.SELECTED)
                .count();

        long rejected = allCandidates.stream()
                .filter(c -> c.getStatus() ==
                        Candidate.CandidateStatus.REJECTED)
                .count();

        return PipelineResponse.builder()
                .jobId(jobId)
                .jobTitle(job.getTitle())
                .totalRounds(job.getTotalRounds())
                .stages(stages)
                .totalCandidates((long) allCandidates.size())
                .selected(selected)
                .rejected(rejected)
                .build();
    }

    // Bulk upload from Excel
    @Transactional
    public Map<String, Object> bulkUpload(
            MultipartFile file, Long jobId, String email) {

        JobOpening job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        User recruiter = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Candidate> candidates = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(
                file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            // Skip header row — start from row 1
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String name = getCellValue(row, 0);
                    String candidateEmail = getCellValue(row, 1);
                    String phone = getCellValue(row, 2);
                    String company = getCellValue(row, 3);
                    String expStr = getCellValue(row, 4);
                    String sourceStr = getCellValue(row, 5);

                    if (name.isEmpty() || candidateEmail.isEmpty()) {
                        errors.add("Row " + (i + 1) +
                                ": Name and email are required");
                        continue;
                    }

                    Candidate.Source source;
                    try {
                        source = Candidate.Source.valueOf(
                                sourceStr.toUpperCase());
                    } catch (Exception e) {
                        source = Candidate.Source.PORTAL;
                    }

                    Integer exp = 0;
                    try {
                        exp = Integer.parseInt(expStr);
                    } catch (Exception ignored) {}

                    candidates.add(Candidate.builder()
                            .name(name)
                            .email(candidateEmail)
                            .phone(phone)
                            .currentCompany(company)
                            .experienceYears(exp)
                            .source(source)
                            .jobOpening(job)
                            .currentStage(0)
                            .addedBy(recruiter)
                            .schedulingToken(
                                    UUID.randomUUID().toString())
                            .build());

                } catch (Exception e) {
                    errors.add("Row " + (i + 1) +
                            ": " + e.getMessage());
                }
            }

            candidateRepository.saveAll(candidates);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to read Excel file: " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("successCount", candidates.size());
        result.put("errorCount", errors.size());
        result.put("errors", errors);
        return result;
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(
                    (int) cell.getNumericCellValue());
            default -> "";
        };
    }
}